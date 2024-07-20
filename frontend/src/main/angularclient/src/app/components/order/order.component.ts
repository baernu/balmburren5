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

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent {
  tourDateBindInfosDTOs: TourDateBindInfosDTO[] = [];
  tourDTOs: TourDTO[] = [];
  orders: OrderDTO[] = [];
  people: UserDTO = new UserDTO();
  userProfileOrders: UserProfileOrderDTO[] = [];


  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }


  async ngOnInit(): Promise<void> {
    let username = localStorage.getItem('username');
    if (username)
      this.people = await firstValueFrom(this.userService.findUser(username));
    this.tourDateBindInfosDTOs = [];
    if (this.people.username)
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
      order.deliverPeople = this.people;
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
    this.orders = await firstValueFrom(this.userService.getAllOrderForPerson(this.people));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.tour.number.localeCompare(t2.tour.number));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));
  }

  async getAllTourBindDates(tour: TourDTO) {
    let date = new Date();
    let dateNow : DatesDTO = new DatesDTO();
    dateNow.date = new Date().toISOString().split('T')[0];
    let dayPlus21 : DatesDTO = new DatesDTO();
    dayPlus21.date = new Date(date.setDate(date.getDate() + 21)).toISOString().split('T')[0];
    dateNow = await firstValueFrom(this.tourService.createDates(dateNow));
    dayPlus21 = await firstValueFrom(this.tourService.createDates(dayPlus21));
    if (await firstValueFrom(this.userService.existPersonBindTour(this.people.username, tour.number))) {
      let tourDateBindInfos: TourDateBindInfosDTO[] = await firstValueFrom(this.tourService.getAllTourDatesBindInfosForTourAndDateBetween(
        tour, dateNow, dayPlus21));
      tourDateBindInfos.forEach(tDBI => this.tourDateBindInfosDTOs.push(tDBI));
    }
  }

  async apply() {
    for (const order of this.orders) {
      await this.putOrder(order);
    }
    await this.router.navigate(['basic_order']);

  }
  private async putOrder(order: OrderDTO) {
    order.isChecked = true;
    let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
      order.productBindInfos.productDetails, order.date, order.tour));
    order.version = order1.version;
    await firstValueFrom(this.userService.putOrder(order));
  }

  private async getAllUserProfileOrders() {
    let uPOFPs : UserProfileOrderDTO[] = await firstValueFrom(this.userService.getAllUserProfileOrderForPerson(this.people));
    uPOFPs.forEach(uPO => this.userProfileOrders.push(uPO));
  }
}
