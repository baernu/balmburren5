import { Component } from '@angular/core';
import {TourDTO} from "../service/TourDTO";
import {TourServiceService} from "../service/tour-service.service";
import {firstValueFrom, from} from "rxjs";

@Component({
  selector: 'app-tour',
  templateUrl: './tour.component.html',
  styleUrls: ['./tour.component.css']
})
export class TourComponent {
  tour: TourDTO;


  constructor(
    private tourService: TourServiceService
  ){
    this.tour = new TourDTO();
  }



  async onSubmit() {
    if (!await this.checkIfExists(this.tour.number))
      await firstValueFrom(this.tourService.createTour(this.tour));
  }

  private async checkIfExists(number: string) {
    let tours = await firstValueFrom(this.tourService.getTours());
    tours = tours.filter(tour => tour.number == number);
    return tours.length > 0;
  }
}
