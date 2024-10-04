import {Component, OnInit} from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {WorkDTO} from "../../tour/service/workDTO";
import {TourServiceService} from "../../tour/service/tour-service.service";

@Component({
  selector: 'app-driver-work',
  templateUrl: './driver-work.component.html',
  styleUrls: ['./driver-work.component.css']
})
export class DriverWorkComponent implements OnInit {
  dates: DatesDTO = new DatesDTO();
  work: WorkDTO = new WorkDTO();
  user: UserDTO = new UserDTO();
  counter: number = 0;
  success: string = "";
  error: string = "";
  param1: string | null = "";

  constructor(private userService: UserService,
              private tourService: TourServiceService,
              private router: Router,
              private route: ActivatedRoute) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;}
  }

  async ngOnInit(){
    this.param1= this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.user = await firstValueFrom(this.userService.findUser(this.param1));
    if (this.user.username != "") this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    if (this.user.username == "")this.error = "User nicht gefunden!";
    else this.success = "User gefunden!";
    setTimeout(() => {
      this.error = "";
      this.success = "";
      return;}, 1000);
  }

  async showWork() {
    this.error = "";
    this.success = "";
    this.work = new WorkDTO();
    this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
    this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
    // let user = await firstValueFrom(this.userService.currentUser());
    // this.user = await firstValueFrom(this.userService.findUser(user.username));
    let work = await firstValueFrom(this.tourService.getWork(this.user.username, this.dates));
    if (work) {
      this.work = work;
    }
    else return;
  }

  async apply() {
    this.work.date = this.dates;
    this.work.user = this.user;
    if (this.work.id != "") {
      try {
        this.work = await firstValueFrom(this.tourService.putWork(this.work));
      } catch (error: any) {
        if (error.status != 200) this.error= "Speichern hat nicht funktioniert!"
        setTimeout(async () => {
          this.error = "";
          return;
          // await this.router.navigate(['/work']);
        }, 2000);
        // await this.router.navigate(['/work']);
        // return;
      }
      this.success = "Speichern hat funktioniert!"
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/driver_work'],
          {
            queryParams: {
              param1: this.param1
            }
          });
      }, 1000);
      // return;
    }
    else {
      try{
        this.work = await firstValueFrom(this.tourService.createWork(this.work));
      }catch (error: any) {
        if (error.status != 200) this.error= "Speichern hat nicht funktioniert!"
        setTimeout(async () => {
          this.error = "";
          // await this.router.navigate(['/work']);
          return;
        }, 1000);
        // await this.router.navigate(['/work']);
        // return;
      }
      this.success = "Speichern hat funktioniert!"
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/driver_work'],
          {
            queryParams: {
              param1: this.param1
            }
          });
      }, 1000);
    }

  }

  async clear() {
    if (this.counter == 6) {
      try {
        await firstValueFrom(this.tourService.deleteWork(this.user.username, this.dates));
      } catch (error: any) {
        if (error.status != 200) this.error = "Arbeit konnte nicht gelöscht werden!";
        setTimeout(() => {
          this.error = "";
          return;
        }, 2000);
      }
      this.success = "Arbeit wurde gelöscht.";
      setTimeout(async () => {
        this.success = "";
        await this.router.navigate(['/driver_work'],
          {
            queryParams: {
              param1: this.param1
            }
          });
      }, 1000);



      // await firstValueFrom(this.tourService.deleteWork(this.user.username, this.dates));
      this.counter = 0;
      // this.success = "Arbeit wurde gelöscht!"

      // return;
    }
    this.counter ++;
    let c = 7 - this.counter;
    this.error = "Tippe " + c +  " mal zum Löschen!";
    setTimeout(() => {
      this.error = "";
      return;
    }, 1000);
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
}

