import { Component } from '@angular/core';
import {DatesDTO} from "../../../admin/components/tour/service/DatesDTO";
import {firstValueFrom} from "rxjs";
import {TourServiceService} from "../../../admin/components/tour/service/tour-service.service";


@Component({
  selector: 'app-work',
  templateUrl: './work.component.html',
  styleUrls: ['./work.component.css']
})
export class WorkComponent {

  dates: DatesDTO = new DatesDTO();

  constructor(
    private tourService: TourServiceService
  ) {
  }


  async showWork(dates: DatesDTO) {
    this.dates.date = new Date(dates.date).toISOString().split('T')[0];
    this.dates = await firstValueFrom(this.tourService.createDates(this.dates));
    if(this.compare(new Date(this.dates.date))){

    }

  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

}
