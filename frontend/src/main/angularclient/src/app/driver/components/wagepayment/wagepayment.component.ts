import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {WorkDTO} from "../../../admin/components/tour/service/workDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";
import {UserService} from "../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import { DriverBindInvoiceDTO } from 'src/app/components/user/service/driverBindInvoiceDTO';
import {InvoiceDTO} from "../../../components/user/service/invoiceDTO";

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
  success2: string = "";
  error2: string = "";
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
    let user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(user.username));
    this.actualdate.date = new Date().toISOString().split('T')[0];
    this.actualdate = await firstValueFrom(this.tourService.createDates(this.actualdate));
    this.startSetting();
  }

  async startSetting() {
    this.driverBindInvoices = await firstValueFrom(this.userService.getAllDriverBindInvoiceForInvoice(this.user));
    this.driverBindInvoices = this.driverBindInvoices.sort((e1 , e2) => e2.dateTo.date.localeCompare(e1.dateTo.date));
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
    }
    this.success = "OK!";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
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
    }
    this.success = "OK!";
    setTimeout(() => {
      this.success = "";
      return;
    }, 1000);
  }

  computeTotalWork() {
    let total: number = 0;
    for (let work of this.works){
      total += parseFloat(work.workTime.replace(':','.'));
    }
    return total.toFixed(2);;
  }

}
