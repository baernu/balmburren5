import {Component, OnInit} from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {UserBindTourDTO} from "../../../../components/user/service/userBindTourDTO";
import {firstValueFrom} from "rxjs";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {TourDTO} from "../../tour/service/TourDTO";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {UserBindTourDTOAdapted} from "../../../../components/user/service/userBindTourDTOAdapted";
import {Router} from "@angular/router";

@Component({
  selector: 'app-userbindtour',
  templateUrl: './userbindtour.component.html',
  styleUrls: ['./userbindtour.component.css']
})
export class UserbindtourComponent implements OnInit{
  user: UserDTO = new UserDTO();
  users: UserDTO[] | undefined;
  userBindTours: UserBindTourDTO[] | undefined;
  userBindTour: UserBindTourDTO = new UserBindTourDTO();
  tours: TourDTO[] | undefined;
  tour: TourDTO = new TourDTO();
  actualTour: TourDTO = new TourDTO();
  dateNow: string = new Date().toISOString().split('T')[0];
  userBindToursAdapt: UserBindTourDTOAdapted[] | undefined;
  error: string = "";
  success: string = "";


  constructor(private userService: UserService,
              private tourService: TourServiceService,
              private router: Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;};
  }

  async ngOnInit() {
    this.users = await firstValueFrom(this.userService.findAll());
    this.tours = await firstValueFrom(this.tourService.getTours());
  }

  async goTo(tour: TourDTO) {
    this.userBindToursAdapt = [];
    this.userBindTours = await firstValueFrom(this.userService.getAllPersonsForTour(tour.number));
    if (this.userBindTours != null){
      this.userBindTours = this.userBindTours.sort((n1: { position: number; }, n2: { position: number; }) =>
        n1.position - n2.position
      );
      this.userBindTours.forEach(userBindTour => {
        let userBindTourAdapt: UserBindTourDTOAdapted = new UserBindTourDTOAdapted();
        if (userBindTour.startDate != null) {
          if (userBindTour.startDate.date != null) userBindTourAdapt.startDate = userBindTour.startDate.date.split("T")[0];
        }
        if (userBindTour.endDate != null){
          if (userBindTour.endDate.date != null) userBindTourAdapt.endDate = userBindTour.endDate.date.split("T")[0];
        }
        userBindTourAdapt.tour = userBindTour.tour;
        userBindTourAdapt.id = userBindTour.id;
        userBindTourAdapt.user = userBindTour.user;
        userBindTourAdapt.version = userBindTour.version;
        userBindTourAdapt.position = userBindTour.position;
        if (this.userBindToursAdapt != null) this.userBindToursAdapt.push(userBindTourAdapt);
      });
    }

    this.actualTour = tour;
  }

  async addPersonBindTour(username: string) {
    let user1 = await firstValueFrom(this.userService.findUser(username));
    this.tour = await firstValueFrom(this.tourService.getTour(this.actualTour.number));
    this.userBindTour.tour = this.tour;
    this.userBindTour.user = user1;
    if (!await firstValueFrom(this.userService.existPersonBindTour(this.userBindTour.user.username, this.userBindTour.tour.number))){
      await firstValueFrom(this.userService.addPersonBindTour(this.userBindTour));
    }
    await this.goTo(this.actualTour);
  }

  apply() {
    if (this.userBindToursAdapt != null)
    this.userBindToursAdapt = this.userBindToursAdapt.sort((n1: { position: number; }, n2: { position: number; }) =>
        n1.position - n2.position
      );

  }

  async save() {
    let i = 1;
    if (this.userBindToursAdapt != null)
    this.userBindToursAdapt.forEach( userBindTour => {
      userBindTour.position = i;
      this.checkDates(userBindTour);
      i++;
    });
  }

  async checkDates(userBindTour: UserBindTourDTOAdapted){
    let userBindTour2: UserBindTourDTO = await firstValueFrom(this.userService.getPersonBindTour(userBindTour.user.username, userBindTour.tour.number));
    let dateDTO1: DatesDTO;
    let dateDTO2: DatesDTO;
    userBindTour2.position = userBindTour.position;
    if (userBindTour.startDate != null) {
      let dateStringStartDate: string = new Date(userBindTour.startDate).toISOString().split("T")[0];
      dateDTO1 = new DatesDTO();
      dateDTO1.date = dateStringStartDate;
      userBindTour2.startDate = await firstValueFrom(this.tourService.createDates(dateDTO1));

    }
    if (userBindTour.endDate != null) {
      let dateStringEndDate: string = new Date(userBindTour.endDate).toISOString().split("T")[0];
      dateDTO2 = new DatesDTO();
      dateDTO2.date = dateStringEndDate;
      userBindTour2.endDate = await firstValueFrom(this.tourService.createDates(dateDTO2));
    }
    try{
      await firstValueFrom(this.userService.updatePersonBindTour(userBindTour2));
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
      this.router.navigate(['/admin_user_bind_tour']);}, 1000);
  }

  async delete(userBindTour: UserBindTourDTOAdapted) {
    try{
      await firstValueFrom(this.userService.deletePersonBindTour(userBindTour.user.username, userBindTour.tour.number));
    }catch(error: any){
      if(error.status != 200) {
        this.error = "Löschen hat nicht geklappt!";
        setTimeout(() => {
          this.error = "";
          }, 2000);
        return;
      }
    }
    this.success = "Löschen hat geklappt";
    setTimeout(() => {
      this.success = "";
      this.router.navigate(['/admin_user_bind_tour']);}, 1000);
  }

  compareDate(userBindTour: UserBindTourDTOAdapted): boolean {
    if (userBindTour.startDate == null && userBindTour.endDate == null) return true;
    if (userBindTour.startDate == null && userBindTour.endDate != null && userBindTour.endDate >= this.dateNow) return true;
    if (userBindTour.startDate != null && userBindTour.startDate <= this.dateNow && userBindTour.endDate == null) return true;
    if (userBindTour.startDate != null && userBindTour.startDate <= this.dateNow && userBindTour.endDate != null && userBindTour.endDate >= this.dateNow) return true;
    return false;
  }
}


