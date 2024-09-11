import {Component, OnInit} from '@angular/core';
import {TourDTO} from "../../../service/TourDTO";
import {TourServiceService} from "../../../service/tour-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {DatesDTO} from "../../../service/DatesDTO";
import {firstValueFrom} from "rxjs";
import {TourBindDatesDTO} from "../../../service/TourBindDatesDTO";

@Component({
  selector: 'app-add-dates',
  templateUrl: './add-dates.component.html',
  styleUrls: ['./add-dates.component.css']
})
export class AddDatesComponent implements OnInit{
  tour: TourDTO;
  datesDTOs: DatesDTO[] = [];
  dates: Date[] = [];
  tourBindDatesDTOs: TourBindDatesDTO[] = [];
  param1: string | null = "";


  constructor(
    private tourService: TourServiceService,
    private route: ActivatedRoute,
    private router: Router,
  ){
    this.tour = new TourDTO();
  }
  async onSubmit() {
    this.param1= this.route.snapshot.queryParamMap.get('param1');
    console.log(this.param1);
    if (this.param1 != null) {
      this.tour = await firstValueFrom(this.tourService.getTour(this.param1));
      //check if the chosen dates are ok
      for (let date of this.dates) {
        if ( date != null) {
          if (this.compare(date)){
            date = new Date(date);
            let dateString: string = date.toISOString().split('T')[0];
            this.tourBindDatesDTOs = await firstValueFrom(this.tourService.getAllTourBindDatesForTour(this.param1));
            if (!this.tourBindDatesDTOs.find(d => d.dates.date === dateString)) {
              let dateDTO1: DatesDTO = new DatesDTO();
              dateDTO1.date = dateString;
              dateDTO1 = await firstValueFrom(this.tourService.createDates(dateDTO1));
              let tourBindDates: TourBindDatesDTO = new TourBindDatesDTO();
              tourBindDates.tour = this.tour;
              tourBindDates.dates = dateDTO1;
              await firstValueFrom(this.tourService.createTourBindDates(tourBindDates));
            }
          }
        }
      }

      // await this.router.navigate(['/admin_tour_data']);
      await this.router.navigate(['admin_tour_data/'],
          {
            queryParams: {
              param1: this.param1
            }
          });
    }
  }


  async ngOnInit(): Promise<void> {
    this.param1= this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.tour = await firstValueFrom(this.tourService.getTour(this.param1));
  }

  public compare(date: Date): boolean
  {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }
}
