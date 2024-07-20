import { Component } from '@angular/core';
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {UserService} from "../user/service/user-service.service";
import {OrderDTO} from "../user/service/orderDTO";
import {firstValueFrom} from "rxjs";
import {UserDTO} from "../user/service/userDTO";
import {DatesDTO} from "../../admin/components/tour/service/DatesDTO";

@Component({
  selector: 'app-invoice',
  templateUrl: './invoice.component.html',
  styleUrls: ['./invoice.component.css']
})
export class InvoiceComponent {
  orders: OrderDTO[] = [];
  username: string = "";
  people: UserDTO = new UserDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  price: number = 0;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService){}

  async ngOnInit(): Promise<void> {
    let username = localStorage.getItem('username');
    if (username)
      this.people = await firstValueFrom(this.userService.findUser(username));
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
