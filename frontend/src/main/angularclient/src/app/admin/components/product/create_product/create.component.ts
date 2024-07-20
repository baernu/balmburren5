import { Component } from '@angular/core';
import {firstValueFrom} from "rxjs";
import {ProductService} from "../service/product.service";
import {ProductDTO} from "../service/ProductDTO";
import {ActivatedRoute, Router} from "@angular/router";

@Component({
  selector: 'app-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.css']
})
export class CreateComponent {
  product: ProductDTO = new ProductDTO();
  constructor(
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router){}



  async onSubmit() {
    if (!await firstValueFrom(this.productService.existProduct(this.product.name)))
      await firstValueFrom(this.productService.createProduct(this.product));
    await this.router.navigate(['admin_product']);
  }

}
