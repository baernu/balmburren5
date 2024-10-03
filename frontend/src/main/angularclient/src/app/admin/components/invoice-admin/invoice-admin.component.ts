import { Component } from '@angular/core';
import {DatesDTO} from "../tour/service/DatesDTO";
import {TourServiceService} from "../tour/service/tour-service.service";
import {UserService} from "../../../components/user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import {AdminInvoiceDTO} from "../../../components/user/service/adminInvoiceDTO";
import {UserDTO} from "../../../components/user/service/userDTO";
import {InvoiceDTO} from "../../../components/user/service/invoiceDTO";
import {OrderDTO} from "../../../components/user/service/orderDTO";
import {UserBindInvoiceDTO} from "../../../components/user/service/userBindInvoiceDTO";
import {UserBindPhoneDTO} from "../../../components/user/service/UserBindPhoneDTO";
import {Router} from "@angular/router";

@Component({
  selector: 'app-invoice-admin',
  templateUrl: './invoice-admin.component.html',
  styleUrls: ['./invoice-admin.component.css']
})
export class InvoiceAdminComponent {
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  invoices: AdminInvoiceDTO[] = [];
  users: UserDTO[] = [];
  userBindInvoices: UserBindInvoiceDTO[] = [];
  userBindPhone: UserBindPhoneDTO = new UserBindPhoneDTO();
  all: Boolean = false;
  success: string = "";
  error: string = "";
  success1: string = "";
  error1: string = "";
  count: number = 0;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    this.users = await firstValueFrom(this.userService.findAll());
    this.users.forEach(user => {
      let invoice: AdminInvoiceDTO = new AdminInvoiceDTO();
      invoice.id = user.id;
      invoice.version = user.version;
      invoice.username = user.username;
      invoice.firstname = user.firstname;
      invoice.lastname = user.lastname;
      this.invoices.push(invoice);
    });
  }

  async apply() {
    this.dateFrom.date = new Date(this.dateFrom.date).toISOString().split('T')[0];
    this.dateFrom = await firstValueFrom(this.tourService.createDates(this.dateFrom));
    this.dateTo.date = new Date(this.dateTo.date).toISOString().split('T')[0];
    this.dateTo = await firstValueFrom(this.tourService.createDates(this.dateTo));
    this.createInvoices();
  }

  async createInvoices() {
    for (const invoice of this.invoices) {
      let invoice1: InvoiceDTO = new InvoiceDTO();
      if(invoice.isChecked) {
        let people: UserDTO = new UserDTO();
        people = await firstValueFrom(this.userService.findUser(invoice.username));
        if (await firstValueFrom(this.userService.existUserBindPhone(people)))
          this.userBindPhone = await firstValueFrom(this.userService.getUserBindPhone(people));
        let orders: OrderDTO[] = await firstValueFrom(this.userService.getAllOrderForPersonBetween(this.dateFrom, this.dateTo, people));
        let amount: number = 0;
        orders.forEach(order => amount += order.quantityDelivered * order.productBindInfos.productDetails.price);
        invoice1.amount = amount;
        if (invoice1.amount > 0) {
          invoice1 = await firstValueFrom(this.userService.createInvoice(invoice1));
          let userBindInvoice: UserBindInvoiceDTO = new UserBindInvoiceDTO();
          userBindInvoice.invoice = invoice1;
          userBindInvoice.personInvoice = this.userBindPhone.invoicePerson;
          userBindInvoice.personDeliver = people;
          userBindInvoice.dateFrom.date = this.dateFrom.date;
          userBindInvoice.dateFrom = await firstValueFrom(this.tourService.createDates(userBindInvoice.dateFrom));
          userBindInvoice.dateTo.date = this.dateTo.date;
          userBindInvoice.dateTo = await firstValueFrom(this.tourService.createDates(userBindInvoice.dateTo));
          let bool = await firstValueFrom(this.userService.existUserBindInvoice(userBindInvoice.dateFrom, userBindInvoice.dateTo,
            userBindInvoice.personInvoice, userBindInvoice.personDeliver));
          // console.log("Bool existuserbindinvoice ", bool);
          if (!bool) {
            try{
              await firstValueFrom(this.userService.createUserBindInvoice(userBindInvoice));
            }catch(error: any){
              if(error.status != 200) {
                this.error = "Erstellen hat nicht geklappt!";
                setTimeout(() => {
                  this.error = "";
                  return;}, 2000);
              }
            }
            this.success = "Erstellen hat geklappt";
            setTimeout(async () => {
              this.success = "";
              await this.router.navigate(['admin_invoice']);
            }, 1000);
          }
        }
      }
    }
  }

  async showInvoices(invoice: AdminInvoiceDTO) {
    let user: UserDTO = await firstValueFrom(this.userService.findUser(invoice.username));
    let userBindInvoices = await firstValueFrom(this.userService.getAllPersonBindInvoiceForDeliver(user));
    if(userBindInvoices) this.userBindInvoices = userBindInvoices;
  }

  async delete(userBindInvoice: UserBindInvoiceDTO) {
    if(this.count == 7){
      try{
        await firstValueFrom(this.userService.deleteUserBindInvoice(userBindInvoice.dateFrom, userBindInvoice.dateTo,
          userBindInvoice.personInvoice, userBindInvoice.personDeliver));
        this.userBindInvoices = await firstValueFrom(this.userService.getAllPersonBindInvoiceForDeliver(userBindInvoice.personDeliver));
      }catch(error: any){
        if(error.status != 200) {
          this.error1 = "Löschen hat nicht geklappt!";
          setTimeout(() => {
            this.error1 = "";
            return;}, 2000);
        }
      }
      this.success1 = "Löschen hat geklappt";
      setTimeout(() => {
        this.success1 = "";
        this.count = 0;
        this.router.navigate(['/admin_user_bind_tour']);}, 1000);
    }else {
      this.count++;
      this.error1 = "Noch "+ (8-this.count) + " mal klicken zum Löschen.";
      setTimeout(() => {
        this.error1 = "";
        return;}, 1000);
    }

  }

  check() {
    if (this.all) {
      for (const invoice of this.invoices) {
        invoice.isChecked = true;
      }
    } else {
      for (const invoice of this.invoices) {
        invoice.isChecked = false;
      }
    }
  }

  saveInvoice1(userBindinvoice: UserBindInvoiceDTO) {
    if(userBindinvoice.invoice.isPaid)userBindinvoice.invoice.isPaid = false;
    else userBindinvoice.invoice.isPaid = true;
    this.saveInvoice(userBindinvoice);
  }
  saveInvoice2(userBindinvoice: UserBindInvoiceDTO) {
    if(userBindinvoice.invoice.isSent)userBindinvoice.invoice.isSent = false;
    else userBindinvoice.invoice.isSent = true;
    this.saveInvoice(userBindinvoice);
  }

  async saveInvoice(userBindInvoice: UserBindInvoiceDTO) {
    try {
      userBindInvoice.invoice = await firstValueFrom(this.userService.putInvoice(userBindInvoice.invoice));
    } catch (error: any) {
      if (error.status != 200) {
        this.error1 = "Speichern hat nicht geklappt!";
        setTimeout(() => {
          this.error1 = "";
          return;
        }, 2000);
      }
    }
    this.success1 = "Speichern hat geklappt.";
    setTimeout(() => {
      this.success1 = "";
      return;
    }, 1000);
  }
}

