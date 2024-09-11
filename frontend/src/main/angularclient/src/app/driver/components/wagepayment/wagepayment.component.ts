import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {WorkDTO} from "../../../admin/components/tour/service/workDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";
import {UserService} from "../../../components/user/service/user-service.service";
import {Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import { DriverBindInvoiceDTO } from 'src/app/components/user/service/driverBindInvoiceDTO';

@Component({
  selector: 'app-wagepayment',
  templateUrl: './wagepayment.component.html',
  styleUrls: ['./wagepayment.component.css']
})
export class WagepaymentComponent {
  dates: DatesDTO = new DatesDTO();
  works: WorkDTO[] = [];
  user: UserDTO = new UserDTO();
  driverBindInvoices: DriverBindInvoiceDTO[] = [];
  enddate: DatesDTO = new DatesDTO();
  startdate: DatesDTO = new DatesDTO();
  actualdate: DatesDTO = new DatesDTO();
  success: string = "";
  error: string = "";
  total: string= "";
  count: number = 0;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private router: Router,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    this.user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    this.actualdate.date = new Date().toISOString().split('T')[0];
    this.actualdate = await firstValueFrom(this.tourService.createDates(this.actualdate));
    this.startSetting();
  }
  async startSetting() {
    this.driverBindInvoices = await firstValueFrom(this.userService.getAllDriverBindInvoiceForInvoice(this.user));
    this.driverBindInvoices = this.driverBindInvoices.sort((e1 , e2) => e1.dateTo.date.localeCompare(e2.dateTo.date));
  }


  async showWork1() {
    this.error = "";
    this.success = "";
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
    this.error = "";
    this.success = "";
    if (this.count == 6){
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
    }
    this.count ++;
    this.error = "Klicke noch " + (7-this.count) + " mal um zu löschen";
    setTimeout(() => {
      this.error = "";
      return;}, 1000);
  }
}
