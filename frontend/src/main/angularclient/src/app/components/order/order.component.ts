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
import {ActivatedRoute, Router} from "@angular/router";
import {groupebyDTO} from "../user/service/groupbyDTO";
import {ProductBindInfosDTO} from "../../admin/components/product/service/ProductBindInfosDTO";
import {ProductService} from "../../admin/components/product/service/product.service";

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.css']
})
export class OrderComponent {
  user: UserDTO = new UserDTO();
  tourDateBindInfosDTOs: TourDateBindInfosDTO[] = [];
  tourDTOs: TourDTO[] = [];
  orders: OrderDTO[] = [];
  userProfileOrders1: UserProfileOrderDTO[] = [];
  userProfileOrders2: UserProfileOrderDTO[] = [];
  productBindInfos: ProductBindInfosDTO[] = [];
  tours: TourDTO[] = [];
  categories: groupebyDTO[] = [];
  error: string = "";
  success: string = "";
  error1: string = "";
  success1: string = "";

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
    let user = await firstValueFrom(this.userService.currentUser());
    if(user)this.user = await firstValueFrom(this.userService.findUser(user.username));
    let tours : TourDTO[] = [];
    this.tours = await firstValueFrom(this.tourService.getTours());
    for (const tour of this.tours) {
      if (await firstValueFrom(this.userService.existPersonBindTour(this.user.username, tour.number)))
        tours.push(tour);
    }
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    this.productBindInfos = this.checkIfProductBindInfosActive(this.productBindInfos);
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.product.name.localeCompare(p2.product.name));
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.productDetails.category.localeCompare(p2.productDetails.category));
    for (const tour of tours) {
      for (const pBI of this.productBindInfos) {
        let userProfileOrder = new UserProfileOrderDTO();
        userProfileOrder.tour = tour;
        userProfileOrder.user = this.user;
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


  async saveProfile() {
    try {
      for(const userProfileOrder of this.userProfileOrders1) {
        let userProfileOrder1 = await firstValueFrom(this.userService.getUserProfileOrder(userProfileOrder.user, userProfileOrder.productBindProductDetails.product,
          userProfileOrder.productBindProductDetails.productDetails, userProfileOrder.tour));
        userProfileOrder.version = userProfileOrder1.version;
        await firstValueFrom(this.userService.putUserProfileOrder(userProfileOrder));
      }

    }catch(error: any){
      if(error.status != 200){
        this.error = "Speichern hat nicht funktioniert!";
        setTimeout(async () => {
          this.success = "";
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Speichern hat funktioniert. Regelmässige Bestellung ist aktiviert.";
    setTimeout(async () => {
      this.success = "";
      this.error = "";
    }, 2500);
  }


  async showList() {
    let date = new Date();
    let dateNow = new DatesDTO()
    let dayPlus21 = new DatesDTO();

    dateNow.date = new Date().toISOString().split('T')[0];
    dayPlus21.date = new Date(date.setDate(date.getDate() + 21)).toISOString().split('T')[0];
    dateNow = await firstValueFrom(this.tourService.createDates(dateNow));
    dayPlus21 = await firstValueFrom(this.tourService.createDates(dayPlus21));
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
    this.orders = await firstValueFrom(this.userService.getAllOrderForPersonBetween(dateNow, dayPlus21, this.user));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.tour.number.localeCompare(t2.tour.number));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));
    this.showGroup();
  }

  async saveOrder(order:OrderDTO){
    try {
      await this.putOrder(order);
    }catch(error: any){
      if(error.status != 200){
        this.error1 = "Speichern hat nicht funktioniert!";
        setTimeout(async () => {
          this.success1 = "";
          this.error1 = "";
        }, 2000);
        return;
      }
    }
    this.success1 = "Speichern hat funktioniert.";
    setTimeout(async () => {
      this.success1 = "";
      this.error1 = "";
      await this.router.navigate(['user_order']);
    }, 800);
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

  async save(order: OrderDTO){
    try {
      await this.putOrder(order);
    }catch(error: any){
      if(error.status != 200){
        this.error = "Speichern hat nicht funktioniert!";
        setTimeout( () => {
          this.success = "";
          this.error = "";
        }, 1000);
        return;
      }
    }
    this.success = "Speichern hat funktioniert.";
    setTimeout( () => {
      this.success = "";
      this.error = "";
    }, 1000);
    return;
  }

}


