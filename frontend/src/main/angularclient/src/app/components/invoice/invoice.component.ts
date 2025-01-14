import { Component } from '@angular/core';
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {UserService} from "../user/service/user-service.service";
import {OrderDTO} from "../user/service/orderDTO";
import {firstValueFrom} from "rxjs";
import {UserDTO} from "../user/service/userDTO";
import {DatesDTO} from "../../admin/components/tour/service/DatesDTO";
import {groupebyDTO} from "../user/service/groupbyDTO";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent {
  orders: OrderDTO[] = [];
  username: string = "";
  user: UserDTO = new UserDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  price: number = 0;
  categories: groupebyDTO[] = [];
  spinner: boolean = false;

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
    let user = await firstValueFrom(this.userService.currentUser());
    if(user)this.user = await firstValueFrom(this.userService.findUser(user.username))
  }

  async apply() {
    this.spinner = true;
    this.dateFrom.date = new Date(this.dateFrom.date).toISOString().split('T')[0];
    this.dateFrom = await firstValueFrom(this.tourService.createDates(this.dateFrom));
    this.dateTo.date = new Date(this.dateTo.date).toISOString().split('T')[0];
    this.dateTo = await firstValueFrom(this.tourService.createDates(this.dateTo));
    this.orders = await firstValueFrom(this.userService.getAllOrderForPersonBetween(this.dateFrom, this.dateTo, this.user));
    this.orders = this.orders.filter(order => order.quantityDelivered > 0);
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.product.name.localeCompare(t2.productBindInfos.product.name));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.productDetails.category.localeCompare(t2.productBindInfos.productDetails.category));
    this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));
    this.orders.forEach(o => this.price += o.productBindInfos.productDetails.price * o.quantityDelivered);

    this.showGroup();
    this.spinner = false;
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
