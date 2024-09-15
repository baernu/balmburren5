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
  // showPassword: boolean = false;
  userProfileOrders: UserProfileOrderDTO[] = [];
  productBindInfos: ProductBindInfosDTO[] = [];
  user: UserDTO = new UserDTO();
  tours: TourDTO[] = [];
  error: string = "";
  success: string = "";

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
    let tours : TourDTO[] = [];
    this.user = await firstValueFrom(this.userService.currentUser());
    this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    // this.user.password = "";
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
        userProfileOrder.user = this.user;
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
    try {
      for(const userProfileOrder of this.userProfileOrders) {
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
          return;
        }, 2000);
      }
    }
    this.success = "Speichern hat funktioniert. Regelmässige Bestellung ist aktiviert.";
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      await this.router.navigate(['basic_order_profil']);
    }, 4000);
  }
}
