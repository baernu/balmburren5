import { Component } from '@angular/core';
import {DatesDTO} from "../../tour/service/DatesDTO";
import {WorkDTO} from "../../tour/service/workDTO";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {DriverBindInvoiceDTO} from "../../../../components/user/service/driverBindInvoiceDTO";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {InvoiceDTO} from "../../../../components/user/service/invoiceDTO";

@Component({
  selector: 'app-driver-overview',
  templateUrl: './driver-overview.component.html',
  styleUrls: ['./driver-overview.component.css']
})
export class DriverOverviewComponent {

  dates: DatesDTO = new DatesDTO();
  works: WorkDTO[] = [];
  user: UserDTO = new UserDTO();
  driverBindInvoices: DriverBindInvoiceDTO[] = [];
  enddate: DatesDTO = new DatesDTO();
  startdate: DatesDTO = new DatesDTO();
  actualdate: DatesDTO = new DatesDTO();
  success: string = "";
  error: string = "";
  success2: string = "";
  error2: string = "";
  total: string= "";
  param1: string | null = "";
  count: number = 0;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private router: Router,
    private route: ActivatedRoute,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    this.param1= this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.user = await firstValueFrom(this.userService.findUser(this.param1));
    if (this.user.username != "") this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    if (this.user.username == "")this.error = "User nicht gefunden!";
    else this.success = "User gefunden!";
    setTimeout(() => {
      this.error = "";
      this.success = "";
      return;}, 1000);
    this.actualdate.date = new Date().toISOString().split('T')[0];
    this.actualdate = await firstValueFrom(this.tourService.createDates(this.actualdate));
    this.startSetting();
  }

  async startSetting() {
    this.driverBindInvoices = await firstValueFrom(this.userService.getAllDriverBindInvoiceForInvoice(this.user));
    this.driverBindInvoices = this.driverBindInvoices.sort((e1 , e2) => e1.dateTo.date.localeCompare(e2.dateTo.date));
  }

  async showWork1() {
    this.startSetting();
    try {
      this.startdate.date = new Date(this.startdate.date).toISOString().split('T')[0];
      let date = new Date(this.startdate.date);
      let date1 = new Date(date.getTime() - 86400000).toISOString().split('T')[0];
      this.startdate = await firstValueFrom(this.tourService.createDates(this.startdate));
      this.works = await firstValueFrom(this.tourService.getAllWorksForUserandIntervall(this.user.username, this.startdate, this.actualdate));
      this.works.sort((e1: WorkDTO, e2: WorkDTO) => e1.date.date.localeCompare(e2.date.date));
      this.enddate = this.actualdate;
      this.total = this.computeTotalWork();
      this.driverBindInvoices = this.driverBindInvoices.filter(e => e.dateTo.date.localeCompare(date1));
    }catch(error: any){
      if(error.status != 200)this.error = "Etwas lief schief!";
      setTimeout(() => {
        this.error = "";
        return;
      }, 2000);
      // return;
    }
    this.success = "OK!";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
    // return;
  }

  async showWork2() {
    try {
      this.enddate.date = new Date(this.enddate.date).toISOString().split('T')[0];
      this.enddate = await firstValueFrom(this.tourService.createDates(this.enddate));
      this.works = await firstValueFrom(this.tourService.getAllWorksForUserandIntervall(this.user.username, this.startdate, this.enddate));
      this.works.sort((e1: WorkDTO, e2: WorkDTO) => e1.date.date.localeCompare(e2.date.date));
      this.total = this.computeTotalWork();
      this.driverBindInvoices = this.driverBindInvoices.filter(e => e.dateTo.date.localeCompare(this.enddate.date));
    }catch(error: any){
      if(error.status != 200)this.error = "Etwas lief schief!";
      setTimeout(() => {
        this.error = "";
        return;
      }, 2000);
      // return;
    }
    this.success = "OK!";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
    // return;
  }

  computeTotalWork() {
    let total: number = 0;
    for (let work of this.works){
      total += parseFloat(work.workTime.replace(':','.'));
    }
    return total.toFixed(2);;
  }

  async deleteWork(work:WorkDTO) {
    if (this.count == 6){
      try{
        await firstValueFrom(this.tourService.deleteWorkById(work));
      }catch(error: any) {
        if (error.status != 200) {
          this.error = "Löschen hat nicht geklappt";
          setTimeout(() => {
            this.error = "";
            return;
          }, 2000);
          // return;
        }
      }
      this.success = "Löschen hat geklappt";
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/driver_overview/'],
          {
            queryParams: {
              param1: this.user.username
            }
          });
        // this.ngOnInit();
      }, 1000);
      // setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
    }else {
      this.count ++;
      this.error = "Klicke noch " + (7-this.count) + " mal um zu löschen";
      setTimeout(() => {
        this.error = "";
        return;}, 1000);
    }
  }

  async putInvoice(driverBindInvoice: DriverBindInvoiceDTO) {
    try{
      driverBindInvoice.invoice = await firstValueFrom(this.userService.putInvoice(driverBindInvoice.invoice));
    }catch(error:any){
      if (error.status != 200) {
        this.error2 = "Lohn Auszahlung konnte nicht aktualisiert werden!";
        setTimeout(() => {
          this.error2 = "";
          return;
        }, 2000);
        // return;
      }
    }
    this.success2 = "Lohn Auszahlung wurde akualisiert!";
    setTimeout(async () => {
      this.success2 = "";
      await this.router.navigate(['/driver_overview/'],
        {
          queryParams: {
            param1: this.user.username
          }
        });
    }, 1000);
    // this.ngOnInit();
    // setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
  }

  async deleteUserBindInvoice(driverBindInvoice: DriverBindInvoiceDTO) {
    try{
      await firstValueFrom(this.userService.deleteDriverBindInvoiceById(driverBindInvoice));
    }catch(error:any){
      if (error.status != 200) {
        this.error2 = "Lohn Erfassung konnte nicht gelöscht werden!";
        setTimeout(() => {
          this.error2 = "";
          return;
        }, 2000);
        // return;
      }
    }
    this.success2 = "Lohn Erfassung wurde gelöscht!";
    setTimeout(async () => {
      this.success2 = "";
      await this.router.navigate(['/driver_overview/'],
        {
          queryParams: {
            param1: this.user.username
          }
        });
    }, 1000);
    // this.ngOnInit();
    // setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
  }

  async createDriverBindInvoice() {
    this.success2 = "";
    this.error2 = "";
    let driverBindInvoice: DriverBindInvoiceDTO = new DriverBindInvoiceDTO();
    let invoice: InvoiceDTO = new InvoiceDTO();
    try{
      invoice = await firstValueFrom(this.userService.createInvoice(invoice));
      invoice.amount = parseFloat(this.total);
      invoice = await firstValueFrom(this.userService.putInvoice(invoice));
      driverBindInvoice.invoice = invoice;
      driverBindInvoice.dateTo = this.enddate;
      driverBindInvoice.dateFrom = this.startdate;
      driverBindInvoice.personInvoice = this.user;
      driverBindInvoice = await firstValueFrom(this.userService.createDriverBindInvoice(driverBindInvoice));
    }catch(error:any){
      if (error.status != 200 || 201) {
        this.error2 = "Lohn Erfassung konnte nicht erstellt werden!";
        setTimeout(() => {
          this.error2 = "";
          return;
        }, 2000);
        // return;
      }
    }
    this.success2 = "Lohn Erfassung wurde erstellt!";
    setTimeout(async () => {
      this.success2 = "";
      await this.router.navigate(['/driver_overview/'],
        {
          queryParams: {
            param1: this.user.username
          }
        });
    }, 1000);
    // this.ngOnInit();
    // setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
  }
}

