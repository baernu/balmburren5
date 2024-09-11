import {Component, OnInit} from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserWithRoleDTO} from "../../../../components/user/service/userWithRoleDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {UserBindRoleDTO} from "../../../../components/user/service/userBindRoleDTO";
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
  }

  async showWork() {
    this.error = "";
    this.success = "";
    this.work = new WorkDTO();
    this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
    this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
    let user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(user.username));
    // if(this.compare(new Date(this.dates.date))){
    console.log("Id Dates: " + this.dates.id);
    let work = await firstValueFrom(this.tourService.getWork(this.user.username, this.dates));
    if (work) {
      this.work = work;
      // this.success = "Arbeit wurde gespeichert!"
    }
    else return;
    // } else this.error ="Datum liegt in der Vergangenheit: Keine Berechtigung!"
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

  // compare(date: Date): boolean {
  //   let now1 = new Date().toISOString().split('T')[0]
  //   date = new Date(date);
  //   return date.toISOString().split('T')[0] >= now1;
  // }

  computeWorktime(){
    let total: number = 0;
    let arr = this.work.endTime.split(':');
    let arr2 = this.work.startTime.split(':');
    // console.log("arr: " + arr[0] + '/ ' + arr[1]);
    // console.log("arr2: " + arr2[0] + '/ ' + arr2[1]);
    total = parseInt(arr[0]) - parseInt(arr2[0]);
    // console.log("total: " + total);
    total = total + (parseFloat(arr[1]) - parseFloat(arr2[1]))/60;
    let str = total.toFixed(2);
    // console.log("startZeit: " + this.work.startTime);
    // console.log("Total: " + str);
    this.work.workTime = str;
  }
}

