import { Component } from '@angular/core';
import {CardDTO} from "../../../admin/components/product/service/cardDTO";
import {firstValueFrom} from "rxjs";
import {UserService} from "../../../components/user/service/user-service.service";
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
}
