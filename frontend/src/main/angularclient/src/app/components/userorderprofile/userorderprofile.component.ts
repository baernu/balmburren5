import { Component } from '@angular/core';
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {UserService} from "../user/service/user-service.service";
import {UserProfileOrderDTO} from "../user/service/userProfileOrderDTO";
import {ProductBindInfosDTO} from "../../admin/components/product/service/ProductBindInfosDTO";
import {firstValueFrom} from "rxjs";
import {ProductService} from "../../admin/components/product/service/product.service";
import {UserDTO} from "../user/service/userDTO";
import {TourDTO} from "../../admin/components/tour/service/TourDTO";
import {ErrorHandlingService} from "../error_handling/error-handling.service";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-userorderprofile',
  templateUrl: './userorderprofile.component.html',
  styleUrls: ['./userorderprofile.component.css']
})
export class UserorderprofileComponent {
  bool2: boolean = false;
  showPassword: boolean = false;
  userProfileOrders: UserProfileOrderDTO[] = [];
  productBindInfos: ProductBindInfosDTO[] = [];
  user: UserDTO = new UserDTO();
  tours: TourDTO[] = [];

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private productService: ProductService,
    private errorHandlingService: ErrorHandlingService,
    private route: ActivatedRoute,
    private router: Router,
    ){
    this.user = new UserDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    let bool2: Boolean = await firstValueFrom(this.errorHandlingService.getBoolRegister2());
    this.bool2 = bool2.valueOf();
    let tours : TourDTO[] = [];
    let username = localStorage.getItem('username');
    if (username)
      this.user = await firstValueFrom(this.userService.findUser(username));
    this.user.password = "";
    this.tours = await firstValueFrom(this.tourService.getTours());
    for (const tour of this.tours) {
      if (await firstValueFrom(this.userService.existPersonBindTour(this.user.username, tour.number)))
        tours.push(tour);
    }
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    this.productBindInfos = this.checkIfProductBindInfosActive(this.productBindInfos);
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.product.name.localeCompare(p2.product.name));
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p2.productDetails.price - p1.productDetails.price);
    for (const tour of tours) {
      for (const pBI of this.productBindInfos) {
        let userProfileOrder = new UserProfileOrderDTO();
        userProfileOrder.tour = tour;
        userProfileOrder.person = this.user;
        userProfileOrder.productBindProductDetails = pBI;
        if (await firstValueFrom(this.userService.existUserProfileOrder(this.user, pBI.product, pBI.productDetails, tour)))
          this.userProfileOrders.push(await firstValueFrom(this.userService.getUserProfileOrder(this.user, pBI.product, pBI.productDetails, tour)));
        else  this.userProfileOrders.push(await firstValueFrom(this.userService.createUserProfileOrder(userProfileOrder)));
      }
    }
  }

  checkIfProductBindInfosActive(productBindInfos: ProductBindInfosDTO[]) {
    return productBindInfos.filter(productBindInfo =>  productBindInfo.endDate.date >= new Date().toISOString().split('T')[0]);
  }

  async save() {
    for(const userProfileOrder of this.userProfileOrders) {
      let userProfileOrder1 = await firstValueFrom(this.userService.getUserProfileOrder(userProfileOrder.person, userProfileOrder.productBindProductDetails.product,
        userProfileOrder.productBindProductDetails.productDetails, userProfileOrder.tour));
      userProfileOrder.version = userProfileOrder1.version;
      await firstValueFrom(this.userService.putUserProfileOrder(userProfileOrder));
    }
  }
  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async onSubmit() {
    if (this.user.password.length > 7){
      await firstValueFrom(this.errorHandlingService.putBoolRegister2(false));
      await firstValueFrom(this.userService.save(this.user));
      await this.router.navigate(['login']);
    } else {
      await firstValueFrom(this.errorHandlingService.putBoolRegister2(true));
      await this.router.navigate(['basic_order_profil']);
    }


  }
}
