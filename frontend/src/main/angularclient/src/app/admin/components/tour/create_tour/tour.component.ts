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
  error: string = "";
  success: string = "";


  constructor(
    private tourService: TourServiceService
  ){
    this.tour = new TourDTO();
  }



  async onSubmit() {
    try{
      if (!await this.checkIfExists(this.tour.number.toString()))
        await firstValueFrom(this.tourService.createTour(this.tour));
    }catch(error: any){
      if(error.status != 200) {
        this.error = "Speichern hat nicht geklappt!";
        setTimeout(() => {
          this.error = "";
         }, 2000);
        return;
      }
    }
    this.success = "Speichern hat geklappt";
    setTimeout(() => {
      this.success = "";
     }, 1000);
  }

  private async checkIfExists(number: string) {
    let tours = await firstValueFrom(this.tourService.getTours());
    tours = tours.filter(tour => tour.number == number);
    return tours.length > 0;
  }
}
