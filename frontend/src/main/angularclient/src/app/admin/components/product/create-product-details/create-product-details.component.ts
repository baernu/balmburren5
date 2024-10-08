import { Component } from '@angular/core';
import {ProductService} from "../service/product.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {ProductDetailsDTO} from "../service/ProductDetailsDTO";

@Component({
  selector: 'app-create-product-details',
  templateUrl: './create-product-details.component.html',
  styleUrls: ['./create-product-details.component.css']
})
export class CreateProductDetailsComponent {
  productDetails: ProductDetailsDTO = new ProductDetailsDTO();
  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router){}



  async onSubmit() {
    await firstValueFrom(this.productService.createProductDetails(this.productDetails));
    await this.router.navigate(['admin_product']);
  }

}
