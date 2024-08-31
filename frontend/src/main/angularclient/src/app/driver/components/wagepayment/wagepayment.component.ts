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
  work: WorkDTO = new WorkDTO();
  user: UserDTO = new UserDTO();
  counter: number = 0;
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


  async showWork() {
    this.error = "";
    this.success = "";
    this.work = new WorkDTO();
    this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
    this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
    let user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(user.username));
    if(this.compare(new Date(this.dates.date))){
      console.log("Id Dates: " + this.dates.id);
      let work = await firstValueFrom(this.tourService.getWork(this.user.username, this.dates));
      if (work) {
        this.work = work;
        // this.success = "Arbeit wurde gespeichert!"
      }
      else return;
    } else this.error ="Datum liegt in der Vergangenheit: Keine Berechtigung!"
  }

  async apply() {
    this.work.date = this.dates;
    this.work.user = this.user;
    if (this.work.id != "") {
      try {
        this.work = await firstValueFrom(this.tourService.putWork(this.work));
      } catch (error: any) {
        if (error.status != 200) this.error= "Speichern hat nicht funktioniert!"
        await this.router.navigate(['/work']);
        return;
      }
      this.success = "Speichern hat funktioniert!"
      return;
    }
    else {
      try{
        this.work = await firstValueFrom(this.tourService.createWork(this.work));
      }catch (error: any) {
        if (error.status != 200) this.error= "Speichern hat nicht funktioniert!"
        await this.router.navigate(['/work']);
        return;
      }
      this.success = "Speichern hat funktioniert!"
      return;
    }

  }

  async clear() {
    if (this.counter == 6) {
      await firstValueFrom(this.tourService.deleteWork(this.user.username, this.dates));
      this.counter = 0;
      this.success = "Arbeit wurde gelöscht!"
      await this.router.navigate(['/work']);
      return;
    }
    this.counter ++;
    let c = 7 - this.counter;
    this.error = "Tippe " + c +  " mal zum Löschen!";
  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

}
