import { Component } from '@angular/core';
import {CardDTO} from "../../admin/components/product/service/cardDTO";
import {ProductService} from "../../admin/components/product/service/product.service";
import {Router} from "@angular/router";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-actual',
  templateUrl: './actual.component.html',
  styleUrls: ['./actual.component.css']
})
export class ActualComponent {
  cards: CardDTO[] = []

  constructor(private productService: ProductService,
              private router: Router) {
  }
  async ngOnInit() {
    this.cards = await firstValueFrom(this.productService.findAllActiveCards(true));
  }
}
