import { Component } from '@angular/core';
import {DatesDTO} from "../../tour/service/DatesDTO";
import {UserBindInvoiceDTO} from "../../../../components/user/service/userBindInvoiceDTO";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {UserService} from "../../../../components/user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-invoice-email',
  templateUrl: './invoice-email.component.html',
  styleUrls: ['./invoice-email.component.css']
})
export class InvoiceEmailComponent {
  dateFrom: DatesDTO = new DatesDTO();
  userBindInvoices: UserBindInvoiceDTO[] = [];
  all: Boolean = false;

  constructor(
    private router: Router,
    private tourService: TourServiceService,
    private userService: UserService) {
  }

  async ngOnInit(): Promise<void> {

  }


  async apply() {
    this.userBindInvoices = await firstValueFrom(this.userService.getAllPersonBindInvoiceDateFrom(this.dateFrom));
  }

  check() {
    if (this.all) {
      for (const userBindInvoice of this.userBindInvoices) {
        userBindInvoice.isChecked = true;
      }
    } else {
      for (const userBindInvoice of this.userBindInvoices) {
        userBindInvoice.isChecked = false;
      }
    }
  }

 async email() {
    for (const userBindInvoice of this.userBindInvoices) {
      await firstValueFrom(this.userService.putUserBindInvoice(userBindInvoice));
    }
    await this.router.navigate(['admin_invoice_email_preview'],
     {
       queryParams: {
         param1: this.dateFrom.date
       }
     });
  }
}
