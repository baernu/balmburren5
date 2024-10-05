import { Component } from '@angular/core';
import {UserService} from "../../components/user/service/user-service.service";
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {Router} from "@angular/router";
import {UserBindTourDTO} from "../../components/user/service/userBindTourDTO";
import {ProductBindInfosDTO} from "../../admin/components/product/service/ProductBindInfosDTO";
import {firstValueFrom} from "rxjs";
import {ProductService} from "../../admin/components/product/service/product.service";

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.css']
})
export class ProductsComponent {

  productBindInfos: ProductBindInfosDTO[] = [];

  constructor(
              private productService: ProductService,) {}

  async ngOnInit() {
    this.productBindInfos = await firstValueFrom(this.productService.getProductBindInfosisChecked(true))
  }



}
