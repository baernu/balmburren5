import { Component } from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {TourDateBindInfosDTO} from "../../tour/service/TourDateBindInfosDTO";
import {TourDTO} from "../../tour/service/TourDTO";
import {OrderDTO} from "../../../../components/user/service/orderDTO";
import {UserProfileOrderDTO} from "../../../../components/user/service/userProfileOrderDTO";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {ProductBindInfosDTO} from "../../product/service/ProductBindInfosDTO";
import {ProductService} from "../../product/service/product.service";

@Component({
  selector: 'app-user-order',
  templateUrl: './user-order.component.html',
  styleUrls: ['./user-order.component.css']
})
export class UserOrderComponent {

  param1: string | null = "";
  user: UserDTO = new UserDTO();
  tourDateBindInfosDTOs: TourDateBindInfosDTO[] = [];
  tourDTOs: TourDTO[] = [];
  orders: OrderDTO[] = [];
  userProfileOrders1: UserProfileOrderDTO[] = [];
  userProfileOrders2: UserProfileOrderDTO[] = [];
  productBindInfos: ProductBindInfosDTO[] = [];
  tours: TourDTO[] = [];

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute,
              private tourService: TourServiceService,
              private productService: ProductService,) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    this.param1= this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.user = await firstValueFrom(this.userService.findUser(this.param1));
    //first part of html
    let tours : TourDTO[] = [];
    this.tours = await firstValueFrom(this.tourService.getTours());
    for (const tour of this.tours) {
      if (await firstValueFrom(this.userService.existPersonBindTour(this.user.username, tour.number)))
        tours.push(tour);
    }
    // console.log("Tours: ", this.tours);
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    this.productBindInfos = this.checkIfProductBindInfosActive(this.productBindInfos);
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.product.name.localeCompare(p2.product.name));
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.productDetails.category.localeCompare(p2.productDetails.category));
    for (const tour of tours) {
      // console.log("ProductBindInfos: ", this.productBindInfos);
      for (const pBI of this.productBindInfos) {
        let userProfileOrder = new UserProfileOrderDTO();
        userProfileOrder.tour = tour;
        userProfileOrder.person = this.user;
        userProfileOrder.productBindProductDetails = pBI;
        if (await firstValueFrom(this.userService.existUserProfileOrder(this.user, pBI.product, pBI.productDetails, tour)))
          this.userProfileOrders1.push(await firstValueFrom(this.userService.getUserProfileOrder(this.user, pBI.product, pBI.productDetails, tour)));
        else  this.userProfileOrders1.push(await firstValueFrom(this.userService.createUserProfileOrder(userProfileOrder)));
      }
    }
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
    for (const order of this.orders) {
      await this.putOrder(order);
    }
    await this.router.navigate(['admin_users_order'],
      {
        queryParams: {
          param1: this.param1
        }
      });
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
    uPOFPs.forEach(uPO => this.userProfileOrders2.push(uPO));
  }

  //checkIfProductBindInfosActive and save methods are of first part html
  private checkIfProductBindInfosActive(productBindInfos: ProductBindInfosDTO[]) {
    return productBindInfos.filter(productBindInfo =>  productBindInfo.endDate.date >= new Date().toISOString().split('T')[0]);
  }
  async save() {
    for (const userProfileOrder of this.userProfileOrders1) {
      let userProfileOrder1 = await firstValueFrom(this.userService.getUserProfileOrder(userProfileOrder.person, userProfileOrder.productBindProductDetails.product,
        userProfileOrder.productBindProductDetails.productDetails, userProfileOrder.tour));
      userProfileOrder.version = userProfileOrder1.version;
      await firstValueFrom(this.userService.putUserProfileOrder(userProfileOrder));
    }
    await this.showList();
  }


  async showList() {
    //second part of html
    this.tourDateBindInfosDTOs = [];
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
        let userProfileOrder = this.userProfileOrders2.find(uPO => uPO.productBindProductDetails.id === order.productBindInfos.id
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
  }
}


