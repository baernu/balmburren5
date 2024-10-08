import { Component } from '@angular/core';
import {CardDTO} from "../../../admin/components/product/service/cardDTO";
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";
import {ProductService} from "../../../admin/components/product/service/product.service";

@Component({
  selector: 'app-card-overview',
  templateUrl: './card-overview.component.html',
  styleUrls: ['./card-overview.component.css']
})
export class CardOverviewComponent {
  cards: CardDTO[] = []

  constructor(private productService: ProductService,
              private router: Router) {
  }
  async ngOnInit() {
    this.cards = await firstValueFrom(this.productService.findAllCards());
  }

  async update(card: CardDTO) {
    this.router.navigate(['kathy_card/'],
      {
        queryParams: {
          param1: card.id
        }
      });
  }

  async delete(card: CardDTO) {
    await firstValueFrom(this.productService.deleteCard(card));
    this.ngOnInit();
  }

  async save(card: CardDTO){
    if(card.isactive)card.isactive = false;
    else card.isactive = true;
    card = await firstValueFrom(this.productService.saveCard(card));
  }
}
