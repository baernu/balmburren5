import { Component } from '@angular/core';
import {CardDTO} from "../../../admin/components/product/service/cardDTO";

import {Router} from "@angular/router";
import {ProductService} from "../../../admin/components/product/service/product.service";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  card: CardDTO = new CardDTO();
  error: string = "";
  success: string = "";

  constructor(
    private productService: ProductService,
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }


  async cardSend() {
    try {
      await firstValueFrom(this.productService.saveCard(this.card));
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
    this.success = "Speichern hat funktioniert.";
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      return;
    }, 1000);
  }

  async uploadFile(event: Event) {
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if (fileList) {
      this.card.base64 = <string>await this.returnBase64String(fileList[0]);
    }
  }

  async returnBase64String(file: File) {
    return new Promise((resolve, reject) => {
      let reader = new FileReader();
      reader.readAsDataURL(file);
      reader.onload = () => {
        resolve(reader.result);
      };
      reader.onerror = reject;
    })
  }



}
