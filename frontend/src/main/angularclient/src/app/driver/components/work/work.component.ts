import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {firstValueFrom} from "rxjs";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";
import {WorkDTO} from "../../../admin/components/tour/service/workDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";


@Component({
  selector: 'app-work',
  templateUrl: './work.component.html',
  styleUrls: ['./work.component.css']
})
export class WorkComponent {
  dates: DatesDTO = new DatesDTO();
  work: WorkDTO = new WorkDTO();
  user: UserDTO = new UserDTO();
  counter: number = 0;
  success: string = "";
  error: string = "";
  param1: string | null = "";

  constructor(private userService: UserService,
              private tourService: TourServiceService,
              private router: Router,) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;}
  }

  async ngOnInit(){
    let user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(user.username));

  }

  async showWork() {
    if(this.compare(new Date(this.dates.date))){
      this.work = new WorkDTO();
      this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
      this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
      let work = await firstValueFrom(this.tourService.getWork(this.user.username, this.dates));
      if (work) {
        this.work = work;
      }
      else return;
    }else {
      this.error = "Keine Berechtigung: Datum liegt in der Vergangenheit!"
      setTimeout( () => {
        this.error = "";
      }, 2000);
    }
  }

  async apply() {
    this.work.date = this.dates;
    this.work.user = this.user;
    if (this.work.id != "") {
      try {
        this.work = await firstValueFrom(this.tourService.putWork(this.work));
      } catch (error: any) {
        if (error.status != 200) this.error= "Speichern hat nicht funktioniert!"
        setTimeout(() => {
          this.error = "";
        }, 2000);
        return;
      }
      this.success = "Speichern hat funktioniert!"
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/work']);
      }, 1000);
      return;
    }
    else {
      try{
        this.work = await firstValueFrom(this.tourService.createWork(this.work));
      }catch (error: any) {
        if (error.status != 200) {
          this.error = "Speichern hat nicht funktioniert!"
          setTimeout( () => {
            this.error = "";
          }, 1000);
          return;
        }
      }
      this.success = "Speichern hat funktioniert!"
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/work']);
      }, 1000);
    }
  }

  async clear() {
    if (this.counter == 6) {
      try {
        await firstValueFrom(this.tourService.deleteWork(this.user.username, this.dates));
      } catch (error: any) {
        if (error.status != 200) {
          this.error = "Arbeit konnte nicht gelöscht werden!";
          setTimeout(() => {
            this.error = "";
          }, 2000);
          return;
        }
      }
      this.success = "Arbeit wurde gelöscht.";
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/work']);
      }, 1000);

      this.counter = 0;
    } else {
      this.counter ++;
      let c = 7 - this.counter;
      this.error = "Tippe " + c +  " mal zum Löschen!";
      setTimeout(() => {
        this.error = "";
      }, 1000);
    }
  }

  computeWorktime(){
    let total: number = 0;
    let arr = this.work.endTime.split(':');
    let arr2 = this.work.startTime.split(':');
    total = parseInt(arr[0]) - parseInt(arr2[0]);
    total = total + (parseFloat(arr[1]) - parseFloat(arr2[1]))/60;
    let str = total.toFixed(2);
    this.work.workTime = str;
  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }
}
