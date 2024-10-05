import { Component } from '@angular/core';
import {CardDTO} from "../../../admin/components/product/service/cardDTO";

@Component({
  selector: 'app-card',
  templateUrl: './card.component.html',
  styleUrls: ['./card.component.css']
})
export class CardComponent {
  card: CardDTO = new CardDTO();


  async cardSend() {

  }

  async uploadFile(event: Event) {
  }


}
