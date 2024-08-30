import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {firstValueFrom} from "rxjs";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";
import {WorkDTO} from "../../../admin/components/tour/service/workDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserService} from "../../../components/user/service/user-service.service";


@Component({
  selector: 'app-work',
  templateUrl: './work.component.html',
  styleUrls: ['./work.component.css']
})
export class WorkComponent {

  dates: DatesDTO = new DatesDTO();
  work: WorkDTO = new WorkDTO();
  user: UserDTO = new UserDTO();

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
  ) {
  }


  async showWork(dates: DatesDTO) {
    this.dates.date = new Date(dates.date).toISOString().split('T')[0];
    this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
    let user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(user.username));
    if(this.compare(new Date(this.dates.date))){
      this.work = await firstValueFrom(this.tourService.getWork(this.user.username, parseInt(dates.id)));
    }
  }

  async apply() {
    if (this.work.id) this.work = await firstValueFrom(this.tourService.putWork(this.work));
    else this.work = await firstValueFrom(this.tourService.createWork(this.work));
  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

}
