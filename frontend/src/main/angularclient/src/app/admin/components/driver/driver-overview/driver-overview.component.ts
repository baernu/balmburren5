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
  // success1: string = "";
  // error1: string = "";
  success2: string = "";
  error2: string = "";
  total: string= "";
  param1: string | null = "";

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
    this.actualdate.date = new Date().toISOString().split('T')[0];
    this.actualdate = await firstValueFrom(this.tourService.createDates(this.actualdate));
    this.driverBindInvoices = await firstValueFrom(this.userService.getAllDriverBindInvoiceForInvoice(this.user));
    this.driverBindInvoices = this.driverBindInvoices.sort((e1 , e2) => e1.dateTo.date.localeCompare(e2.dateTo.date));
  }


  async showWork1() {
    this.error = "";
    this.success = "";
    try {
      this.startdate.date = new Date(this.startdate.date).toISOString().split('T')[0];
      this.startdate = await firstValueFrom(this.tourService.createDates(this.startdate));
      this.works = await firstValueFrom(this.tourService.getAllWorksForUserandIntervall(this.user.username, this.startdate, this.actualdate));
      this.works.sort((e1: WorkDTO, e2: WorkDTO) => e1.date.date.localeCompare(e2.date.date));
      this.enddate = this.actualdate;
      this.total = this.computeTotalWork();
      this.driverBindInvoices = this.driverBindInvoices.filter(e => e.dateTo.date.localeCompare(this.startdate.date));
    }catch(error: any){
      if(error.status != 200)this.error = "Etwas lief schief!";
      return;
    }
    this.success = "OK!";
    return;
  }

  async showWork2() {
    this.error = "";
    this.success = "";
    try {
      this.enddate.date = new Date(this.enddate.date).toISOString().split('T')[0];
      this.enddate = await firstValueFrom(this.tourService.createDates(this.enddate));
      this.works = await firstValueFrom(this.tourService.getAllWorksForUserandIntervall(this.user.username, this.startdate, this.enddate));
      this.works.sort((e1: WorkDTO, e2: WorkDTO) => e1.date.date.localeCompare(e2.date.date));
      this.total = this.computeTotalWork();
      this.driverBindInvoices = this.driverBindInvoices.filter(e => e.dateTo.date.localeCompare(this.enddate.date));
    }catch(error: any){
      if(error.status != 200)this.error = "Etwas lief schief!";
      return;
    }
    this.success = "OK!";
    return;
  }

  computeTotalWork() {
    let total: number = 0;
    for (let work of this.works){
      total += parseFloat(work.workTime.replace(':','.'));
    }
    return total.toFixed(2);;
  }

  async deleteWork(work:WorkDTO) {
    try{
      await firstValueFrom(this.tourService.deleteWorkById(work));
    }catch(error: any) {
      if (error.status != 200) {
        this.error = "Löschen hat nicht geklappt";
        return;
      }
    }
    this.success = "Löschen hat geklappt";
    setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);

    // this.router.navigate(['/wage_payment']);
  }

  async putInvoice(driverBindInvoice: DriverBindInvoiceDTO) {
    this.success2 = "";
    this.error2 = "";
    try{
      driverBindInvoice.invoice = await firstValueFrom(this.userService.putInvoice(driverBindInvoice.invoice));
    }catch(error:any){
      if (error.status != 200) {
        this.error2 = "Lohn Auszahlung konnte nicht aktualisiert werden!";
        return;
      }
    }
    this.success2 = "Lohn Auszahlung wurde akualisiert!";
    setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
  }

  async deleteUserBindInvoice(driverBindInvoice: DriverBindInvoiceDTO) {
    this.success2 = "";
    this.error2 = "";
    try{
      await firstValueFrom(this.userService.deleteDriverBindInvoiceById(driverBindInvoice));
    }catch(error:any){
      if (error.status != 200) {
        this.error2 = "Lohn Erfassung konnte nicht gelöscht werden!";
        return;
      }
    }
    this.success2 = "Lohn Erfassung wurde gelöscht!";
    setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
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
        return;
      }
    }
    this.success2 = "Lohn Erfassung wurde erstellt!";
    setTimeout(() => { this.router.navigate(['/wage_payment']);}, 1000);
  }
}

