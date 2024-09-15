import { Component } from '@angular/core';
import {TourServiceService} from "../tour/service/tour-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {ProductDTO} from "./service/ProductDTO";
import {ProductDetailsDTO} from "./service/ProductDetailsDTO";
import {ProductBindInfosDTO} from "./service/ProductBindInfosDTO";
import {ProductService} from "./service/product.service";
import {firstValueFrom} from "rxjs";
import {DatesDTO} from "../tour/service/DatesDTO";

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent {
  product: ProductDTO = new ProductDTO();
  products: ProductDTO[] = [];
  productDetails: ProductDetailsDTO[] = [];
  productBindInfo: ProductBindInfosDTO = new ProductBindInfosDTO();
  productBindInfos: ProductBindInfosDTO[] = [];
  category: string = "";
  success: string = "";
  error: string = "";
  success1: string = "";
  error1: string = "";


  constructor(
    private tourService: TourServiceService,
    private productService: ProductService,
    private route: ActivatedRoute,
    private router: Router){}

  async ngOnInit() {
    this.products = await firstValueFrom(this.productService.getProducts());
    this.productDetails = await firstValueFrom(this.productService.getAllProductDetails());
    this.productDetails.sort((p1: ProductDetailsDTO, p2: ProductDetailsDTO) => p1.category.localeCompare(p2.category));
    this.productDetails.sort((p1: ProductDetailsDTO, p2: ProductDetailsDTO) => { return p1.size - p2.size});
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p1.product.name.localeCompare(p2.product.name));
    this.productBindInfos.sort((p1:ProductBindInfosDTO, p2:ProductBindInfosDTO) => p2.productDetails.price - p1.productDetails.price);
  }

  addDetails() {
    this.router.navigate(['admin_product_details_create']);
  }

  addProduct() {
    this.router.navigate(['admin_product_create']);
  }

  async createProductBindDetail(productDetail: ProductDetailsDTO) {
    this.productBindInfo.productDetails = productDetail;
    this.productBindInfo.product = this.product;
    let date: DatesDTO = new DatesDTO();
    let dateNow = new Date().toISOString().split('T')[0];
    date.date = dateNow;
    this.productBindInfo.startDate = await firstValueFrom(this.tourService.createDates(date));
    this.productBindInfo.endDate = await firstValueFrom(this.tourService.createDates(date));
    let bool = await firstValueFrom(this.productService.isProductBindInfos(this.productBindInfo.product, this.productBindInfo.productDetails));
    if (!bool)
      await firstValueFrom(this.productService.createProductBindInfos(this.productBindInfo));
    await this.ngOnInit();
  }

  holdProduct(product: ProductDTO) {
    this.product = product;
  }

  async delete(productBindInfo: ProductBindInfosDTO) {
    await firstValueFrom(this.productService.deleteProductBindInfos(productBindInfo.product, productBindInfo.productDetails));
    await this.ngOnInit();
  }

  async save() {
    for(const productBindInfo of this.productBindInfos){
      productBindInfo.startDate = await firstValueFrom(this.tourService.createDates(productBindInfo.startDate));
      productBindInfo.endDate = await firstValueFrom(this.tourService.createDates(productBindInfo.endDate));
      await firstValueFrom(this.productService.putProductBindInfos(productBindInfo));
      await this.ngOnInit();
    }
  }

  async deleteProductDetail(productDetail: ProductDetailsDTO) {
    try {
      await firstValueFrom(this.productService.deleteProductDetails(productDetail));
    }catch(error: any){
      if(error.status != 200){
        this.error1 = "Löschen hat nicht funktioniert!";
        setTimeout(async () => {
          this.success1 = "";
          this.error1 = "";
          return;
        }, 2000);
      }
    }
    this.success1 = "Löschen hat funktioniert.";
    setTimeout(async () => {
      this.success1 = "";
      this.error1 = "";
      this.ngOnInit();
      return;
      // await this.router.navigate(['admin_product']);
    }, 2000);
  }

}
