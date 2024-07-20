import { Component } from '@angular/core';
import {OrderDTO} from "../../../../components/user/service/orderDTO";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {UserService} from "../../../../components/user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-user-ordered',
  templateUrl: './user-ordered.component.html',
  styleUrls: ['./user-ordered.component.css']
})
export class UserOrderedComponent {
  param1: string | null = "";
  orders: OrderDTO[] = [];
  username: string = "";
  people: UserDTO = new UserDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  price: number = 0;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private route: ActivatedRoute,
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    this.param1 = this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.people = await firstValueFrom(this.userService.findUser(this.param1));
  }

  async apply() {
    this.dateFrom.date = new Date(this.dateFrom.date).toISOString().split('T')[0];
    this.dateFrom = await firstValueFrom(this.tourService.createDates(this.dateFrom));
    this.dateTo.date = new Date(this.dateTo.date).toISOString().split('T')[0];
    this.dateTo = await firstValueFrom(this.tourService.createDates(this.dateTo));
    this.orders = await firstValueFrom(this.userService.getAllOrderForPersonBetween(this.dateFrom, this.dateTo, this.people));
    this.orders = this.orders.filter(order => order.quantityDelivered > 0);
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.product.name.localeCompare(t2.productBindInfos.product.name));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.productDetails.category.localeCompare(t2.productBindInfos.productDetails.category));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));
    this.orders.forEach(o => this.price += o.productBindInfos.productDetails.price * o.quantityDelivered);
  }
}
