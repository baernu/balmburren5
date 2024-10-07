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
import {groupebyDTO} from "../../../../components/user/service/groupbyDTO";
import {UserOrderTourAddressDTO} from "../../../../components/user/service/userOrderTourAddressDTO";
import {ProductBindInfoCountDTO} from "../../product/service/productBindInfoCountDTO";

@Component({
  selector: 'app-user-tour',
  templateUrl: './user-tour.component.html',
  styleUrls: ['./user-tour.component.css']
})
export class UserTourComponent {
  orders: OrderDTO[] = [];
  grouped: any;
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
  error: string ="";
  // error1: string ="";
  error2: string ="";
  error3: string ="";
  success: string ="";
  // success1: string ="";
  success2: string ="";
  success3: string ="";
  updatedOrder: boolean = false;
  categories: groupebyDTO[] = [];
  userBindAddress: UserBindDeliverAddressDTO[] = [];
  userOrderTourAddress: UserOrderTourAddressDTO[] = [];
  productBindInfoCounts: ProductBindInfoCountDTO[] = [];


  constructor(
      private tourService: TourServiceService,
      private userService: UserService,
      private emailService: EmailService,
      private productService: ProductService) {
  }

  async ngOnInit(): Promise<void> {
    this.tours = await firstValueFrom(this.tourService.getTours());
    let productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
    productBindInfos = this.checkIfProductBindInfosActive(productBindInfos);
    for (let pBI of productBindInfos){
      // pBI = await firstValueFrom(this.productService.getProductBindInfosById(parseInt(pBI.id)));
      pBI.isChecked = true;
      await firstValueFrom(this.productService.putProductBindInfos(pBI));
    }
  }

  async apply() {
    this.success ="";
    this.error = "";
    for (const userOrderTourAddress of this.userOrderTourAddress) {
      let order = userOrderTourAddress.order;
      let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
          order.productBindInfos.productDetails, order.date, order.tour));
      userOrderTourAddress.order.version = order1.version;
      try {
        await firstValueFrom(this.userService.putOrder(userOrderTourAddress.order));

      } catch (error: any) {
        if (error.status !== 200)
          this.error = "Order wurde nicht upgedated, Name: " + order.deliverPeople.firstname + ' ' + order.deliverPeople.lastname;
        return;
      }
    }
    this.success = "Orders wurden gespeichert!";
    setTimeout(() => {
      this.success = "";
      return;
      }, 1000);
  }

  async goTo(tour: TourDTO) {
    this.error = "";
    if ( tour) {
      this.tour = tour;
      this.tour = await firstValueFrom(this.tourService.getTour(tour.number));


      this.userBindTours = await firstValueFrom(this.userService.getAllPersonsForTour(this.tour.number));
      this.userBindTours.sort((u1: UserBindTourDTO, u2: UserBindTourDTO) => u1.position - u2.position);
      this.dates.date = new Date(this.dates.date).toISOString().split('T')[0];
      this.dates = await firstValueFrom(this.tourService.createDates(this.dates));

      await this.updateAutomatedOrder(this.tour);

      this.orders = await firstValueFrom(this.userService.getAllOrderForTourAndDate(this.tour, this.dates));
      this.orders = this.orders.filter(order => order.quantityOrdered > 0);

      this.orders.sort((o1: OrderDTO, o2: OrderDTO) => o1.productBindInfos.product.name.localeCompare(o2.productBindInfos.product.name));
      this.orders.sort((o1: OrderDTO, o2: OrderDTO) => o1.productBindInfos.productDetails.category.localeCompare(o2.productBindInfos.productDetails.category));
      this.orders.sort((o1: OrderDTO, o2: OrderDTO) => this.orderPositionOfOrder(o1, o2));

    }

    // let eggs: number = 0;
    // let milks: number = 0;
    let productBindProductInfos = await firstValueFrom(this.productService.getProductBindInfosisChecked(true));
    for (const order of this.orders) {
      for (const productBindInfo of productBindProductInfos) {
        if(productBindInfo.id == order.productBindInfos.id){
          let element = this.productBindInfoCounts.find(e => e.productbindinfos.id == order.productBindInfos.id);
          if(element == null){
            let pBIC = new ProductBindInfoCountDTO();
            pBIC.productbindinfos = productBindInfo;
            pBIC.counter = order.quantityOrdered;
            this.productBindInfoCounts.push(pBIC);
          }else {
            element.counter += order.quantityOrdered;
          }
        }
      }
      // if (order.productBindInfos.productDetails.category === "Eier")
      //   eggs += order.quantityOrdered;
      // if (order.productBindInfos.productDetails.category === "Milch")
      //   milks += order.quantityOrdered;
    }
    // this.totalMilk = milks;
    // this.totalEggs = eggs;
    await this.pushUserOrderTourAddress();

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


  async showOrders(tour: TourDTO) {
    await this.goTo(tour);
    this.tour = tour;
    this.showGroup();
  }

  // async onSubmit() {
  //   // this.success2 ="";
  //   // this.error2 = "";
  //   let count: number = 0;
  //   let androidClients: AndroidClientDTO[] = [];
  //   for (const userOrderTourAddress of this.userOrderTourAddress) {
  //     let order = userOrderTourAddress.order;
  //     let androidClient: AndroidClientDTO = new AndroidClientDTO();
  //     let client = androidClients.find(client => client.name === order.deliverPeople.firstname + ' ' + order.deliverPeople.lastname);
  //     if (client) {
  //       if (order.productBindInfos.product.name === "Eier")
  //         client.eggs = order.quantityOrdered.toString();
  //       if (order.productBindInfos.product.name === "Milch")
  //         client.milk = order.quantityOrdered.toString();
  //       client.keys = client.keys.concat(";").concat(order.productBindInfos.id);
  //     } else {
  //       let userBindAddress: UserBindDeliverAddressDTO = await firstValueFrom(this.userService.getUserBindAddress(order.deliverPeople));
  //       androidClient.keys = order.deliverPeople.id + ";" + order.tour.number + ";" + order.productBindInfos.id;
  //       androidClient.date = order.date.date;
  //       androidClient.name = order.deliverPeople.firstname + ' ' + order.deliverPeople.lastname;
  //       androidClient.position = count.toString();
  //       androidClient.address = userBindAddress.address.street + ' ' + userBindAddress.address.number + ', ' +
  //           userBindAddress.address.plz + ' ' + userBindAddress.address.city;
  //       if (order.productBindInfos.product.name === "Eier")
  //         androidClient.eggs = order.quantityOrdered.toString();
  //       if (order.productBindInfos.product.name === "Milch")
  //         androidClient.milk = order.quantityOrdered.toString();
  //       androidClient.geopoint = userBindAddress.address.alatitude + ',' + userBindAddress.address.alongitude;
  //       androidClient.isDelivered = "0";
  //       androidClient.text = "";
  //       androidClients.push(androidClient);
  //       count++;
  //     }
  //
  //   }
  //   let string = await firstValueFrom(this.emailService.sendTourData(androidClients));
  //   this.emailData.filename = "tourData.txt";
  //   this.emailData.type = "attachment";
  //   string = JSON.stringify(string);
  //   const byteArray = new TextEncoder().encode(string);
  //   this.handleChunk(byteArray);
  //   this.emailData.fromEmail = "balmburren@gmail.com";
  //   this.emailData.subject = "Tour-Daten";
  //   this.emailData.body = "Guten Tag \n Sende Ihnen im Anhang die Tour-Daten.";
  //   try {
  //     await firstValueFrom(this.emailService.sendEmail(this.emailData));
  //   } catch (error: any) {
  //     if (error.status != 200) this.error2 = "Email konnte nicht gesendet werden!";
  //   }
  //   this.success2 = "Email konnte gesendet werden!";
  //   setTimeout(() => {
  //     this.success2 = "";
  //     return;
  //   }, 1000);
  // }
  //
  // handleChunk(buf: Uint8Array) {
  //   let fileByteArray = [];
  //   for (let i = 0; i < buf.length; i++) {
  //     fileByteArray.push(buf[i]);
  //   }
  //   this.emailData.byteArray = fileByteArray;
  // }
  //
  // async uploadFile(event: Event) {
  //   this.success3 ="";
  //   this.error3 = "";
  //   let file: File;
  //   let filename: string;
  //   const element = event.currentTarget as HTMLInputElement;
  //   let fileList: FileList | null = element.files;
  //   if (fileList) {
  //     let orders: OrderDTO[] = [];
  //     file = fileList[0];
  //     filename = fileList[0].name;
  //
  //     const fileData: string = await this.fileToString(file);
  //     console.log("filedata " + fileData);
  //     if (fileData) {
  //       let clients: AndroidClientDTO[] = await firstValueFrom(this.emailService.retourTourData(fileData));
  //       let dateDTO: DatesDTO = new DatesDTO();
  //       dateDTO.date = clients[0].date;
  //       dateDTO = await firstValueFrom(this.tourService.createDates(dateDTO));
  //       this.dates = dateDTO;
  //       let bool: boolean = true;
  //       for (const client of clients) {
  //         let keys: string = client.keys;
  //         let str: string[] = keys.split(';');
  //         let tour: TourDTO = await firstValueFrom(this.tourService.getTour(str[1]));
  //         if (bool) {
  //           this.tour = tour;
  //           bool = false;
  //         }
  //         const [, ...rest1] = str;
  //         const [, ...rest] = rest1;
  //         for (const id of rest) {
  //           try {
  //             let productbindinfo: ProductBindInfosDTO = await firstValueFrom(this.productService.getProductBindInfosById(Number(id)));
  //             let user: UserDTO = await firstValueFrom(this.userService.findUserById(Number(str[0])));
  //             let order: OrderDTO = await firstValueFrom(this.userService.getOrder(user, productbindinfo.product,
  //                 productbindinfo.productDetails, dateDTO, tour));
  //             if (client.isDelivered === "2" && order.productBindInfos.product.name === "Milch" || "Wiesenmilch")
  //               order.quantityDelivered = Number(client.milk);
  //             if (client.isDelivered === "2" && order.productBindInfos.product.name === "Eier")
  //               order.quantityDelivered = Number(client.eggs);
  //             if (client.text)
  //               order.text = client.text;
  //             orders.push(order);
  //           }catch(error: any) {
  //             if (error.status != 200) this.error3 = "Upload hat nicht funktioniert!";
  //           }
  //         }
  //       }
  //     }
  //   }
  // }

  updateOrders(orders: OrderDTO[]) {
    for(const order of orders) {
      let userOrderTourAddress = this.userOrderTourAddress.find(e => e.order.id === order.id);
      if(userOrderTourAddress) userOrderTourAddress.order = order;
      else {
        this.error3 = "Upload hat nicht funktioniert!"
        return;
      }
    }
    this.success3 = "Upload hat funktioniert!";
    setTimeout(() => {
      this.success3 = "";
      return;
    }, 1000);
    this.success ="";
  }


  async fileToString(file: File) {
    const fileToBlob = async (file: any) => new Blob([new Uint8Array(await file.arrayBuffer())], {type: file.type});
    let blob = await fileToBlob(file);
    return blob.text();
  }



  async updateAutomatedOrder(tour: TourDTO) {

    let date = new Date();
    let dateNow: DatesDTO = new DatesDTO();
    dateNow.date = new Date().toISOString().split('T')[0];
    let dayPlus21: DatesDTO = new DatesDTO();
    dayPlus21.date = new Date(date.setDate(date.getDate() + 21)).toISOString().split('T')[0];
    if (this.compare(new Date(this.dates.date)) && this.dates.date <= dayPlus21.date) {
      let tourBindInfos: TourDateBindInfosDTO[] = await firstValueFrom(this.tourService.getAllTourDatesBindInfosForTourAndDate(tour, this.dates));
      let userBindTourDTOS = await firstValueFrom(this.userService.getAllPersonsForTour(tour.number));
      for (const userBindTour of userBindTourDTOS) {


       let productBindInfos = await firstValueFrom(this.productService.getAllProductBindInfos());
       productBindInfos = this.checkIfProductBindInfosActive(productBindInfos);
       // for (let pBI of productBindInfos){
       //   pBI = await firstValueFrom(this.productService.getProductBindInfosById(parseInt(pBI.id)));
       //   pBI.isChecked = true;
       //   await firstValueFrom(this.productService.putProductBindInfos(pBI));
       // }

        for (const pBI of productBindInfos) {
          let userProfileOrder = new UserProfileOrderDTO();
          userProfileOrder.tour = tour;
          userProfileOrder.user = userBindTour.user;
          userProfileOrder.productBindProductDetails = pBI;
          if (!await firstValueFrom(this.userService.existUserProfileOrder(userBindTour.user, pBI.product, pBI.productDetails, tour)))
            await firstValueFrom(this.userService.createUserProfileOrder(userProfileOrder));
        }


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
          } else
            order = await firstValueFrom(this.userService.createOrder(order));
          if (!order.isChecked) {
            let userProfileOrder: UserProfileOrderDTO = await firstValueFrom(this.userService.getUserProfileOrder(userBindTour.user,
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

  checkIfProductBindInfosActive(productBindInfos: ProductBindInfosDTO[]) {
    return productBindInfos.filter(productBindInfo =>  productBindInfo.endDate.date >= new Date().toISOString().split('T')[0]);
  }


  private async putOrder(order: OrderDTO) {
    order.isChecked = true;
    let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
        order.productBindInfos.productDetails, order.date, order.tour));
    order.version = order1.version;
    await firstValueFrom(this.userService.putOrder(order));
  }


  async reset() {
    this.success ="";
    this.error = "";
    this.counter++;
    if (this.counter == 7){
      for (let userOrderTourAddress of this.userOrderTourAddress) {
        let order = userOrderTourAddress.order;
        order = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product, order.productBindInfos.productDetails,
            order.date, order.tour));
        order.quantityDelivered = 0;
        try {
          userOrderTourAddress.order = await firstValueFrom(this.userService.putOrder(order));
        } catch (error:any) {
          if (error.status !== 200) this.error = "Das Zurücksetzen hat bei Ordered nicht geklappt, Username: " + order.deliverPeople.username;
          return;
        }
        this.success = "Reset war erfolgreich!";
        setTimeout(() => {
          this.success = "";
          return;
        }, 1000);
        this.apply();
      }
      this.counter ++;
    }
    this.error = "Klicke noch " + (7-this.counter) + " mal um zu löschen";
    setTimeout(() => {
      this.error = "";
      return;}, 1000);
  }

  compare(date: Date): boolean {
    let now1 = new Date().toISOString().split('T')[0]
    date = new Date(date);
    return date.toISOString().split('T')[0] >= now1;
  }

  showGroup() {
    const group = this.userOrderTourAddress.reduce((acc: any, curr) => {
      let key = curr.order.deliverPeople.firstname + ' ' + curr.order.deliverPeople.lastname;
      if (!acc[key]) {
        acc[key] = [];
      }
      acc[key].push(curr);
      return acc;
    }, {});

    //Get the categories and product related.
    this.categories = Object.keys(group).map(key => ({
      category: key,
      products: group[key],
    }));
  }

  async pushUserOrderTourAddress() {
    this.userOrderTourAddress = [];
    for (let order of this.orders) {
      let userOrderTourAddress = new UserOrderTourAddressDTO();
      userOrderTourAddress.order = order;
      let userBindAddress = await firstValueFrom(this.userService.getUserBindAddress(order.deliverPeople));
      userOrderTourAddress.address = userBindAddress.address;
      let userBindTours = await firstValueFrom(this.userService.getAllPersonsForTour(this.tour.number));
      let userBindTour = userBindTours.find(uBT => uBT.user.id === order.deliverPeople.id);
      if (userBindTour) userOrderTourAddress.userbindtour = userBindTour;
      this.userOrderTourAddress.push(userOrderTourAddress);

    }
  }

  googleMaps(userOrderTourAdress: UserOrderTourAddressDTO) {
    let lat = userOrderTourAdress.address.alatitude;
    let lon = userOrderTourAdress.address.alongitude;
    console.log(lat + ','+ lon);

    let combi = lat + ',' + lon;
    window.open('https://www.google.com/maps/search/?api=1&query=' + combi);

  }

  // changeValue(userOrderTourAddress: UserOrderTourAddressDTO) {
  //   userOrderTourAddress.order.quantityDelivered = userOrderTourAddress.order.quantityOrdered;
  //
  //
  //   // this.save(userOrderTourAddress);
  //
  //
  //   return userOrderTourAddress.order.quantityOrdered;
  // }

  async  save1(userOrderTourAddress: UserOrderTourAddressDTO) {
    this.error = "";
    let order = userOrderTourAddress.order;
    let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
      order.productBindInfos.productDetails, order.date, order.tour));
    userOrderTourAddress.order.version = order1.version;
    try {
      await firstValueFrom(this.userService.putOrder(userOrderTourAddress.order));

    } catch (error: any) {
      if (error.status !== 200) {
        this.error = "Order wurde nicht upgedated, Name: " + order.deliverPeople.firstname + ' ' + order.deliverPeople.lastname;
        setTimeout(() => {
          this.error = "";
          return;
        }, 1000);
      }
    }
  }

  async save(userOrderTourAddresses: UserOrderTourAddressDTO[]) {
    this.error = "";

    for(const userOrderTourAddress of userOrderTourAddresses){
      let order = userOrderTourAddress.order;
      let order1: OrderDTO = await firstValueFrom(this.userService.getOrder(order.deliverPeople, order.productBindInfos.product,
        order.productBindInfos.productDetails, order.date, order.tour));
      userOrderTourAddress.order.version = order1.version;
      userOrderTourAddress.order.quantityDelivered = userOrderTourAddress.order.quantityOrdered;
      try {
        await firstValueFrom(this.userService.putOrder(userOrderTourAddress.order));

      } catch (error: any) {
        if (error.status !== 200) {
          this.error = "Order wurde nicht upgedated, Name: " + order.deliverPeople.firstname + ' ' + order.deliverPeople.lastname;
          setTimeout(() => {
            this.error = "";
            return;
          }, 1000);
        }
      }
    }


  }
}
