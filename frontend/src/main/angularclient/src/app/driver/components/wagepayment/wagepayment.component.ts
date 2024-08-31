import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {WorkDTO} from "../../../admin/components/tour/service/workDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";
import {UserService} from "../../../components/user/service/user-service.service";
import {Router} from "@angular/router";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-wagepayment',
  templateUrl: './wagepayment.component.html',
  styleUrls: ['./wagepayment.component.css']
})
export class WagepaymentComponent {
  dates: DatesDTO = new DatesDTO();
  works: WorkDTO[] = [];
  user: UserDTO = new UserDTO();
  enddate: DatesDTO = new DatesDTO();
  startdate: DatesDTO = new DatesDTO();
  actualdate: DatesDTO = new DatesDTO();
  success: string = "";
  error: string = "";

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
  }


  async showWork1() {
    this.error = "";
    this.success = "";
    try {
      this.works = await firstValueFrom(this.tourService.getAllWorksForUserandIntervall(this.user.username, this.startdate, this.actualdate));
    }catch(error: any){
      if(error.status != 200)this.error = "Etwas lief schief!";
    }
    this.success = "OK!";
    return;
  }

  async showWork2() {
    this.error = "";
    this.success = "";

  }




}
