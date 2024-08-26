import { Component } from '@angular/core';
import {OrderDTO} from "../../../../components/user/service/orderDTO";
import {TourServiceService} from "../service/tour-service.service";
import {UserService} from "../../../../components/user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import {TourDTO} from "../service/TourDTO";
import {DatesDTO} from "../service/DatesDTO";
import {UserBindTourDTO} from "../../../../components/user/service/userBindTourDTO";
import {AndroidClientDTO} from "../service/androidClientDTO";
import {UserBindDeliverAddressDTO} from "../../../../components/user/service/userBindDeliverAddressDTO";
import {EmailService} from "../../email/email-service/email.service";
import {EmailDataDTO} from "../../email/email-service/EmailDataDTO";
import {ProductBindInfosDTO} from "../../product/service/ProductBindInfosDTO";
import {ProductService} from "../../product/service/product.service";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {TourDateBindInfosDTO} from "../service/TourDateBindInfosDTO";
import {UserProfileOrderDTO} from "../../../../components/user/service/userProfileOrderDTO";

@Component({
  selector: 'app-user-tour',
  templateUrl: './user-tour.component.html',
  styleUrls: ['./user-tour.component.css']
})
export class UserTourComponent {
  orders: OrderDTO[] = [];
  tour: TourDTO = new TourDTO();
  tours: TourDTO[] = [];
  dates: DatesDTO = new DatesDTO();
  userBindTours: UserBindTourDTO[] = [];
  userBindTour: UserBindTourDTO = new UserBindTourDTO();
  emailData: EmailDataDTO = new EmailDataDTO();
  totalMilk: number = 0;
  totalEggs: number = 0;
  counter: number = 0;
  count: number = 0;
  error: any;
  error1: any;
  error2: any;
  success: any;
  success1: any;

  constructor(
    private tourService: TourServiceService,
    private userService: UserService,
    private emailService: EmailService,
    private productService: ProductService){}

  async ngOnInit(): Promise<void> {
    this.tours = await firstValueFrom(this.tourService.getTours());
  }

  async apply() {
    for(const order of this.orders) {
      let order1 : OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
        order.productBindInfos.productDetails, order.date, order.tour));
      order.version = order1.version;
      try{
        await firstValueFrom(this.userService.putOrder(order));
      }catch(error){
        // @ts-ignore
        if (error.status !== 200)
          this.error = "Order wurde nicht upgedated, username: " + order.deliverPeople.username;
          return;
      }

    }
    this.success = "Orders wurden gespeichert!";
  }

  async goTo(tour: TourDTO) {
    if (tour && this.dates.date){
      this.tour = tour;
      this.tour = await firstValueFrom(this.tourService.getTour(tour.number));
      this.userBindTours = await firstValueFrom(this.userService.getAllPersonsForTour(this.tour.number));
      this.userBindTours.sort((u1: UserBindTourDTO, u2: UserBindTourDTO) => u1.position - u2.position);
      this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
      this.dates = await firstValueFrom(this.tourService.createDates(this.dates));

      await this.updateAutomatedOrder();

      this.orders = await firstValueFrom(this.userService.getAllOrderForTourAndDate(this.tour, this.dates));
      this.orders = this.orders.filter(order => order.quantityOrdered > 0);

      this.orders.sort((o1: OrderDTO, o2: OrderDTO) => o1.productBindInfos.product.name.localeCompare(o2.productBindInfos.product.name));
      this.orders.sort((o1: OrderDTO, o2: OrderDTO) => o1.productBindInfos.productDetails.category.localeCompare(o2.productBindInfos.productDetails.category));
      this.orders.sort(  (o1: OrderDTO, o2: OrderDTO) =>  this.orderPositionOfOrder(o1, o2));}

      let eggs: number = 0;
      let milks: number = 0;
      for (const order of this.orders) {
        if (order.productBindInfos.product.name === "Eier")
          eggs += order.quantityOrdered;
        if (order.productBindInfos.product.name === "Milch" || "Wiesenmilch")
          milks += order.quantityOrdered;
      }
      this.totalMilk = milks;
      this.totalEggs = eggs;

  }
  orderPositionOfOrder(o1: OrderDTO, o2: OrderDTO) {
    let uB1 = new UserBindTourDTO();
    let uB2 = new UserBindTourDTO();
    let u1 = this.userBindTours.find(u => u.user.id === o1.deliverPeople.id);
    if (u1) uB1 = u1;
    let u2 = this.userBindTours.find(u => u.user.id === o2.deliverPeople.id);
    if (u2) uB2 = u2;
    if (!u1 || !u2) return 0;
    return uB1.position - uB2.position;
  }

  findUserBind(order: OrderDTO) {
    let userBindTour = this.userBindTours.find(uBT => uBT.user.id === order.deliverPeople.id);
    if (userBindTour) this.userBindTour = userBindTour;
    if (userBindTour) return true;
    else return false;
  }

  async showOrders() {
    await this.goTo(this.tour);
  }

  async onSubmit() {
    let count: number = 0;
    let androidClients : AndroidClientDTO[] = [];
    for (const order of this.orders) {
      let androidClient: AndroidClientDTO = new AndroidClientDTO();
      let client = androidClients.find(client => client.name === order.deliverPeople.firstname + ' '+ order.deliverPeople.lastname);
      if (client) {
        console.log("in the second client ...");
        if(order.productBindInfos.product.name === "Eier")
          client.eggs = order.quantityOrdered.toString();
        if(order.productBindInfos.product.name === "Milch" || "Wiesenmilch")
          client.milk = order.quantityOrdered.toString();
        client.keys = client.keys.concat(";").concat(order.productBindInfos.id);
      } else {
        let userBindAddress : UserBindDeliverAddressDTO = await firstValueFrom(this.userService.getUserBindAddress(order.deliverPeople));
        androidClient.keys = order.deliverPeople.id + ";" + order.tour.number + ";" + order.productBindInfos.id;
        androidClient.date = order.date.date;
        androidClient.name = order.deliverPeople.firstname + ' '+ order.deliverPeople.lastname;
        androidClient.position = count.toString();
        androidClient.address = userBindAddress.address.street + ' ' + userBindAddress.address.number + ', ' +
          userBindAddress.address.plz + ' ' + userBindAddress.address.city;
        if(order.productBindInfos.product.name === "Eier")
          androidClient.eggs = order.quantityOrdered.toString();
        if(order.productBindInfos.product.name === "Milch" || "Wiesenmilch")
          androidClient.milk = order.quantityOrdered.toString();
        androidClient.geopoint = userBindAddress.address.alatitude + ',' +  userBindAddress.address.alongitude;
        androidClient.isDelivered = "0";
        androidClient.text = "";
        androidClients.push(androidClient);
        count++;
      }

    }
    let string = await firstValueFrom(this.emailService.sendTourData(androidClients));
    this.emailData.filename = "tourData.txt";
    this.emailData.type = "attachment";
    string = JSON.stringify(string);
    const byteArray = new TextEncoder().encode(string);
    this.handleChunk(byteArray);
    // this.emailData.password = "1234656";
    this.emailData.fromEmail = "balmburren@gmail.com";
    this.emailData.subject = "Tour-Daten";
    this.emailData.body = "Guten Tag \n Sende Ihnen im Anhang die Tour-Daten.";
    try{
      await firstValueFrom(this.emailService.sendEmail(this.emailData));
    }catch(error) {
      // @ts-ignore
      if(error.status != 200) this.error2 = "Email konnte nicht gesendet werden!";
    }

  }

  handleChunk(buf: Uint8Array) {
    let fileByteArray = [];
    for (let i = 0; i < buf.length; i++) {
      fileByteArray.push(buf[i]);
    }
    this.emailData.byteArray = fileByteArray;
  }

  async uploadFile(event: Event) {
    let file : File;
    let filename : string;
    const element = event.currentTarget as HTMLInputElement;
    let fileList: FileList | null = element.files;
    if(fileList) {
      let orders: OrderDTO[] = [];
      file = fileList[0];
      filename = fileList[0].name;

      const fileData : string  = await this.fileToString(file);
      console.log("filedata " + fileData);
      if (fileData) {
        let clients : AndroidClientDTO[] = await firstValueFrom(this.emailService.retourTourData(fileData));
        let dateDTO: DatesDTO = new DatesDTO();
        dateDTO.date = clients[0].date;
        dateDTO = await firstValueFrom(this.tourService.createDates(dateDTO));
        this.dates = dateDTO;
        let bool: boolean = true;
        for (const client of clients) {
          let keys: string = client.keys;
          let str: string[] = keys.split(';');
          let tour : TourDTO = await firstValueFrom(this.tourService.getTour(str[1]));
          if (bool) {
            this.tour = tour;
            bool = false;
          }
          const [, ...rest1] = str;
          const [, ...rest] = rest1;
          for (const id of rest){
            let productbindinfo: ProductBindInfosDTO = await firstValueFrom(this.productService.getProductBindInfosById(Number(id)));
            let user : UserDTO = await firstValueFrom(this.userService.findUserById(Number(str[0])));
            let order : OrderDTO = await firstValueFrom(this.userService.getOrder(user, productbindinfo.product,
              productbindinfo.productDetails, dateDTO, tour));
            if(client.isDelivered === "2" && order.productBindInfos.product.name === "Milch" || "Wiesenmilch")
              order.quantityDelivered = Number(client.milk);
            if(client.isDelivered === "2" && order.productBindInfos.product.name === "Eier")
              order.quantityDelivered = Number(client.eggs);
            if(client.text)
              order.text = client.text;
            orders.push(order);
          }
        }
      }
      this.orders = orders;
    }
  }

  async fileToString(file: File) {
    const fileToBlob = async (file: any) => new Blob([new Uint8Array(await file.arrayBuffer())], {type: file.type});
    let blob = await fileToBlob(file);
    return blob.text();

  }

  async sendData() {
    ////////////////////////////////////////////////////////
    await this.updateAutomatedOrder();
    ///////////////////////////////////////////////////////////////7
    for(const order of this.orders) {
      await firstValueFrom(this.userService.putOrder(order));
    }
  }
  async updateAutomatedOrder() {
    let date = new Date();
    let dateNow : DatesDTO = new DatesDTO();
    dateNow.date = new Date().toISOString().split('T')[0];
    let dayPlus21 : DatesDTO = new DatesDTO();
    dayPlus21.date = new Date(date.setDate(date.getDate() + 21)).toISOString().split('T')[0];
    if(this.compare(new Date(this.dates.date)) && this.dates.date <= dayPlus21.date) {
      let tourBindInfos : TourDateBindInfosDTO[] = await firstValueFrom(this.tourService.getAllTourDatesBindInfosForTourAndDate(this.tour, this.dates));
      let userBindTourDTOS = await firstValueFrom(this.userService.getAllPersonsForTour(this.tour.number));
      for (const userBindTour of userBindTourDTOS) {
        for (const tDBI of tourBindInfos) {
          let order = new OrderDTO();
          let date = new DatesDTO();
          date.date = tDBI.dates.date;
          order.date = await firstValueFrom(this.tourService.createDates(date));
          order.deliverPeople = userBindTour.user;
          order.productBindInfos = tDBI.productBindInfos;
          order.tour = tDBI.tour;
          if (await firstValueFrom(this.userService.existOrder(order.deliverPeople, order.productBindInfos.product,
            order.productBindInfos.productDetails, order.date, order.tour))) {
            order = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
              order.productBindInfos.productDetails, order.date, order.tour));
          }
          else
            order = await firstValueFrom(this.userService.createOrder(order));
          if (!order.isChecked) {
            let userProfileOrder : UserProfileOrderDTO = await firstValueFrom(this.userService.getUserProfileOrder(userBindTour.user,
              tDBI.productBindInfos.product, tDBI.productBindInfos.productDetails, tDBI.tour));
            if (new Date(order.date.date).getDay() <= 3 && userProfileOrder) {
              order.quantityOrdered = userProfileOrder.firstWeekOrder;
            } else {
              if (new Date(order.date.date).getDay() > 3 && userProfileOrder)
                order.quantityOrdered = userProfileOrder.secondWeekOrder;
            }
            await this.putOrder(order);
          }
        }
      }
    }
  }
  private async putOrder(order: OrderDTO) {
    order.isChecked = true;
    let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
      order.productBindInfos.productDetails, order.date, order.tour));
    order.version = order1.version;
    await firstValueFrom(this.userService.putOrder(order));
  }
  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

  async commitAll() {
    this.count++;
    if(this.count == 7)
      for(let order of this.orders) {
        order = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product, order.productBindInfos.productDetails,
          order.date, order.tour));
        order.quantityDelivered = order.quantityOrdered;
        // await firstValueFrom(this.userService.putOrder(order));
        try{
          await firstValueFrom(this.userService.putOrder(order));
        }catch(error){
          // @ts-ignore
          if(error.status !== 200) this.error1 = "Das Speichern hat bei Ordered nicht geklappt, Username: " + order.deliverPeople.username;
          return;
        }
        this.success1 = "Speichern war erfolgreich!";
        await this.showOrders();
      }
  }

  async reset() {
    this.counter++;
    if (this.counter == 7)
      for(let order of this.orders) {
        order = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product, order.productBindInfos.productDetails,
          order.date, order.tour));
        order.quantityDelivered = 0;
        try{
          await firstValueFrom(this.userService.putOrder(order));
        }catch(error){
          // @ts-ignore
          if(error.status !== 200) this.error1 = "Das Zurücksetzen hat bei Ordered nicht geklappt, Username: " + order.deliverPeople.username;
          return;
        }
        this.success1 = "Reset war erfolgreich!";

        await this.showOrders();
      }
  }
}
