import { Component } from '@angular/core';
import {firstValueFrom} from "rxjs";
import {DatesDTO} from "../../admin/components/tour/service/DatesDTO";
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {TourDTO} from "../../admin/components/tour/service/TourDTO";
import {UserService} from "../user/service/user-service.service";
import {TourDateBindInfosDTO} from "../../admin/components/tour/service/TourDateBindInfosDTO";
import {OrderDTO} from "../user/service/orderDTO";
import {UserDTO} from "../user/service/userDTO";
import {UserProfileOrderDTO} from "../user/service/userProfileOrderDTO";
import {Router} from "@angular/router";
import {groupebyDTO} from "../user/service/groupbyDTO";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent {
  tourDateBindInfosDTOs: TourDateBindInfosDTO[] = [];
  tourDTOs: TourDTO[] = [];
  orders: OrderDTO[] = [];
  user: UserDTO = new UserDTO();
  userProfileOrders: UserProfileOrderDTO[] = [];
  error: string = "";
  success: string = "";
  categories: groupebyDTO[] = [];


  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }


  async ngOnInit(): Promise<void> {

    this.user= await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(this.user.username));

    if (this.user.username)
      await this.getAllUserProfileOrders();
    this.tourDTOs = await firstValueFrom(this.tourService.getTours());
    for (const tour of this.tourDTOs) {
      await this.getAllTourBindDates(tour);
    }
    for (const tDBI of this.tourDateBindInfosDTOs) {
      let order = new OrderDTO();
      let date = new DatesDTO();
      date.date = tDBI.dates.date;
      order.date = await firstValueFrom(this.tourService.createDates(date));
      order.deliverPeople = this.user;
      order.productBindInfos = tDBI.productBindInfos;
      order.tour = tDBI.tour;
      if (await firstValueFrom(this.userService.existOrder(order.deliverPeople, order.productBindInfos.product,
        order.productBindInfos.productDetails, order.date, order.tour))) {
        order = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
          order.productBindInfos.productDetails, order.date, order.tour));
      }
      else
        order = await firstValueFrom(this.userService.createOrder(order));
      if (!order.isChecked) {
        let userProfileOrder = this.userProfileOrders.find(uPO => uPO.productBindProductDetails.id === order.productBindInfos.id
          && uPO.tour.number === order.tour.number);
        if (new Date(order.date.date).getDay() <= 3 && userProfileOrder) {
          order.quantityOrdered = userProfileOrder.firstWeekOrder;
        } else {
          if (new Date(order.date.date).getDay() > 3 && userProfileOrder)
            order.quantityOrdered = userProfileOrder.secondWeekOrder;
        }
        await this.putOrder(order);
      }
    }
    this.orders = await firstValueFrom(this.userService.getAllOrderForPerson(this.user));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.tour.number.localeCompare(t2.tour.number));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));
    this.showGroup();
  }

  async getAllTourBindDates(tour: TourDTO) {
    let date = new Date();
    let dateNow : DatesDTO = new DatesDTO();
    dateNow.date = new Date().toISOString().split('T')[0];
    let dayPlus21 : DatesDTO = new DatesDTO();
    dayPlus21.date = new Date(date.setDate(date.getDate() + 21)).toISOString().split('T')[0];
    dateNow = await firstValueFrom(this.tourService.createDates(dateNow));
    dayPlus21 = await firstValueFrom(this.tourService.createDates(dayPlus21));
    if (await firstValueFrom(this.userService.existPersonBindTour(this.user.username, tour.number))) {
      let tourDateBindInfos: TourDateBindInfosDTO[] = await firstValueFrom(this.tourService.getAllTourDatesBindInfosForTourAndDateBetween(
        tour, dateNow, dayPlus21));
      tourDateBindInfos.forEach(tDBI => this.tourDateBindInfosDTOs.push(tDBI));
    }
  }

  async apply() {
    try {
      for (const order of this.orders) {
        await this.putOrder(order);
      }
    }catch(error: any){
      if(error.status != 200){
        this.error = "Speichern hat nicht funktioniert!";
        setTimeout(async () => {
          this.success = "";
          this.error = "";
          return;
        }, 2000);
      }
    }
    this.success = "Speichern hat funktioniert.";
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      await this.router.navigate(['basic_order']);
    }, 2000);
  }
  private async putOrder(order: OrderDTO) {
    order.isChecked = true;
    let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
      order.productBindInfos.productDetails, order.date, order.tour));
    order.version = order1.version;
    await firstValueFrom(this.userService.putOrder(order));
  }

  private async getAllUserProfileOrders() {
    let uPOFPs : UserProfileOrderDTO[] = await firstValueFrom(this.userService.getAllUserProfileOrderForPerson(this.user));
    uPOFPs.forEach(uPO => this.userProfileOrders.push(uPO));
  }

  showGroup() {
    const group = this.orders.reduce((acc: any, curr) => {
      let key = curr.date.date;
      if (!acc[key]) {
        acc[key] = [];
      }
      acc[key].push(curr);
      return acc;
    }, {});

    //Get the categories and product related.
    this.categories = Object.keys(group).map(key => ({
      category: key,
      products: group[key],
    }));
  }
}

