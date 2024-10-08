import { Component } from '@angular/core';
import {CardDTO} from "../../../admin/components/product/service/cardDTO";
import {ActivatedRoute, Router} from "@angular/router";
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
  param1: string | null = "";

  constructor(
    private productService: ProductService,
    private router: Router,
    private route: ActivatedRoute){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {

    this.param1 = this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.card = await firstValueFrom(this.productService.findCardById(this.param1));
  }


  async cardPut() {
    try {
      await firstValueFrom(this.productService.saveCard(this.card));
    }catch(error: any){
      if(error.status != 200){
        this.error = "Speichern hat nicht funktioniert!";
        setTimeout( () => {
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Speichern hat funktioniert.";
    setTimeout( () => {
      this.success = "";
    }, 1000);
  }
}
