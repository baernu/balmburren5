import { Component } from '@angular/core';
import {TourServiceService} from "../tour/service/tour-service.service";
import {Router} from "@angular/router";
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
  success2: string = "";
  error2: string = "";


  constructor(
    private tourService: TourServiceService,
    private productService: ProductService,
    private router: Router){}

  async ngOnInit() {
    this.products = await firstValueFrom(this.productService.getProducts());
    this.productDetails = await firstValueFrom(this.productService.getAllProductDetails());
    this.productDetails.sort((p1: ProductDetailsDTO, p2: ProductDetailsDTO) => p1.category.localeCompare(p2.category));
    this.productDetails.sort((p1: ProductDetailsDTO, p2: ProductDetailsDTO) => { return p1.size - p2.size});
    this.productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    let productBindInfos = this.checkIfProductBindInfosActive(this.productBindInfos);
    for (let pBI of productBindInfos){
      pBI = await firstValueFrom(this.productService.getProductBindInfosById(parseInt(pBI.id)));
      pBI.isChecked = true;
      await firstValueFrom(this.productService.putProductBindInfos(pBI));
    }
    let productBindInfosPassive = this.checkIfProductBindInfosPassive(this.productBindInfos);
    for (let pBI of productBindInfosPassive){
      pBI = await firstValueFrom(this.productService.getProductBindInfosById(parseInt(pBI.id)));
      pBI.isChecked = false;
      await firstValueFrom(this.productService.putProductBindInfos(pBI));
    }
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
    try{
      await firstValueFrom(this.productService.deleteProductBindInfos(productBindInfo));
    }catch(error: any) {
      if(error.status != 200){
        this.error2 = "Löschen hat nicht funktioniert!";
        setTimeout(async () => {
          this.error2 = "";
        }, 2000);
        return;
      }
    }
    this.success2 = "Löschen hat funktioniert.";
    setTimeout(async () => {
      this.success2 = "";
      this.ngOnInit();
    }, 2000);
  }

  async save() {
    try {
      for(const productBindInfo of this.productBindInfos){
        productBindInfo.startDate = await firstValueFrom(this.tourService.createDates(productBindInfo.startDate));
        productBindInfo.endDate = await firstValueFrom(this.tourService.createDates(productBindInfo.endDate));
        await firstValueFrom(this.productService.putProductBindInfos(productBindInfo));
        await this.ngOnInit();
      }
    }catch(error: any){
      if(error.status != 200 || 201){
        this.error2 = "Speichern hat nicht funktioniert!";
        setTimeout( () => {
          this.error2 = "";
        }, 2000);
        return;
      }
    }
    this.success2 = "Daten wurden gespeichert.";
    setTimeout(async () => {
      this.success2 = "";
      await this.ngOnInit();
    }, 2000);
  }

  async deleteProductDetail(productDetail: ProductDetailsDTO) {
    try {
      await firstValueFrom(this.productService.deleteProductDetails(productDetail));
    }catch(error: any){
      if(error.status != 200){
        this.error1 = "Löschen hat nicht funktioniert!";
        setTimeout(async () => {
          this.error1 = "";
        }, 2000);
        return;
      }
    }
    this.success1 = "Löschen hat funktioniert.";
    setTimeout(async () => {
      this.success1 = "";
      this.ngOnInit();
    }, 2000);
  }

  async deleteProduct(product: ProductDTO) {
    try {
      await firstValueFrom(this.productService.deleteProduct(product));
    }catch(error: any){
      if(error.status != 200){
        this.error = "Löschen hat nicht funktioniert!";
        setTimeout( () => {
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Löschen hat funktioniert.";
    setTimeout(async () => {
      this.success = "";
      this.ngOnInit();
    }, 2000);
  }


  checkIfProductBindInfosActive(productBindInfos: ProductBindInfosDTO[]) {
    return productBindInfos.filter(productBindInfo =>  this.compare(new Date(productBindInfo.endDate.date)));
  }

  checkIfProductBindInfosPassive(productBindInfos: ProductBindInfosDTO[]) {
    // return productBindInfos.filter(productBindInfo =>  productBindInfo.endDate.date < new Date().toISOString().split('T')[0]);
    return productBindInfos.filter(productBindInfo =>  !this.compare(new Date(productBindInfo.endDate.date)));
  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

}
