import { Component } from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserBindInvoiceDTO} from "../../../../components/user/service/userBindInvoiceDTO";
import {DatesDTO} from "../../tour/service/DatesDTO";
import {OrderDTO} from "../../../../components/user/service/orderDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {TourServiceService} from "../../tour/service/tour-service.service";
import {InvoiceDTO} from "../../../../components/user/service/invoiceDTO";
import {EmailService} from "../../email/email-service/email.service";
import {EmailDataDTO} from "../../email/email-service/EmailDataDTO";
import { jsPDF } from "jspdf";
import {InvoiceQRDTO} from "../../email/email-service/invoiceQRDTO";
import {UserBindDeliverAddressDTO} from "../../../../components/user/service/userBindDeliverAddressDTO";
import {ReferenceDTO} from "../../../../components/user/service/ReferenceDTO";
import {UserBindPhoneDTO} from "../../../../components/user/service/UserBindPhoneDTO";

@Component({
  selector: 'app-invoice-email-preview',
  templateUrl: './invoice-email-preview.component.html',
  styleUrls: ['./invoice-email-preview.component.css']
})
export class InvoiceEmailPreviewComponent {
  user: UserDTO = new UserDTO();
  dateFrom: DatesDTO = new DatesDTO();
  dateTo: DatesDTO = new DatesDTO();
  invoice: InvoiceDTO = new InvoiceDTO();
  userBindInvoices: UserBindInvoiceDTO[] = [];
  orders: OrderDTO[] = [];
  param1: string | null = "";
  price: number = 0;
  emailData: EmailDataDTO = new EmailDataDTO();
  userBindInvoice: UserBindInvoiceDTO = new UserBindInvoiceDTO();
  private reference: string = "";

  constructor(private userService: UserService,
              private tourService: TourServiceService,
              private emailService: EmailService,
              private router: Router,
              private route: ActivatedRoute ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {
    let button1 = document.getElementById("button_send");
    if(button1)
      button1.style.visibility = 'visible';
    let button2 = document.getElementById("button_notsend");
    if(button2)
      button2.style.visibility = 'visible';
    this.param1 = this.route.snapshot.queryParamMap.get('param1');
    let date: DatesDTO = new DatesDTO();
    if (this.param1 != null) {
      date.date = this.param1;
    }
      date = await firstValueFrom(this.tourService.createDates(date));
    this.userBindInvoices = await firstValueFrom(this.userService.getAllPersonBindInvoiceDateFrom(date));
    this.userBindInvoices = this.userBindInvoices.filter(userBindInvoice => userBindInvoice.isChecked);
    if (this.userBindInvoices.length > 0) {
      this.user = this.userBindInvoices[0].personDeliver;
      console.log("user is: ", this.user);
      await this.setOrders(this.user);
    }
    else {
      await this.router.navigate(['admin_invoice_email']);
    }
  }

  async setOrders(user: UserDTO) {
    let userBindInvoices: UserBindInvoiceDTO[] = await firstValueFrom(this.userService.getAllPersonBindInvoiceForDeliver(this.user));
    let userBindInvoice = userBindInvoices.find(userBindInvoice => userBindInvoice.dateFrom.date === this.param1);
    if (userBindInvoice) {
      this.userBindInvoice = userBindInvoice;
      this.orders = await firstValueFrom(this.userService.getAllOrderForPersonBetween(userBindInvoice.dateFrom, userBindInvoice.dateTo, user));
      this.orders = this.orders.filter(order => order.quantityDelivered > 0);
      this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.product.name.localeCompare(t2.productBindInfos.product.name));
      this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.productBindInfos.productDetails.category.localeCompare(t2.productBindInfos.productDetails.category));
      this.orders.sort((t1: OrderDTO, t2: OrderDTO) => t1.date.date.localeCompare(t2.date.date));

      this.dateFrom = userBindInvoice.dateFrom;
      this.dateTo = userBindInvoice.dateTo;
    }
    this.orders.forEach(o => this.price += o.productBindInfos.productDetails.price * o.quantityDelivered);

  }

  async sendToEmail() {
    let userBindPhone : UserBindPhoneDTO = await firstValueFrom(this.userService.getUserBindPhone(this.user));
    this.emailData.base64String = await this.Convert_HTML_To_PDF();

    this.emailData.fromEmail = "balmburren@gmail.com";
    // this.emailData.toEmail = "bernhard.messerli.5@gmail.com";
    this.emailData.toEmail = userBindPhone.email;
    this.emailData.subject = "Rechnung Balmburren";
    this.emailData.body ="Guten Tag " + this.user.firstname +' ' + this.user.lastname + " Balmburren sendet Ihnen die Rechnung im Anhang. \n Freundliche Grüsse Balmburren";
    // this.emailData.password = "123456";
    this.emailData.filename = "Balmburren.pdf";

    console.log("Email string: " + this.emailData.base64String);
    await firstValueFrom(this.emailService.sendEmail(this.emailData));
    await this.sendQRInvoice();
    await this.notsendEmail();
  }

  async notsendEmail() {
    this.userBindInvoice.isChecked = Boolean(false);
    await firstValueFrom(this.userService.putUserBindInvoice(this.userBindInvoice));
    await this.ngOnInit();
  }


  Convert_HTML_To_PDF(){
    let button1 = document.getElementById("button_send");
    if(button1)
      button1.style.visibility = 'hidden';
    let button2 = document.getElementById("button_notsend");
    if(button2)
      button2.style.visibility = 'hidden';
    return new Promise((resolve, reject) => {
    let doc = new jsPDF();

    let elementHTML = document.getElementById("content_html");
    if(elementHTML)
    doc.html(elementHTML, {
      callback: function(doc) {
        // Save the PDF
        doc.save('Balmburren_Rechnung.pdf');
        let out = doc.output('blob');
        let reader = new FileReader();

        reader.readAsDataURL(out);
        reader.onloadend = function () { // for blob to base64
          resolve(reader.result);
        }
        reader.onerror = reject;
      },
      margin: [10, 10, 10, 10],
      autoPaging: 'text',
      x: 0,
      y: 0,
      width: 190, //target width in the PDF document
      windowWidth: 675 //window width in CSS pixels
    });
    })
  }

  async getInvoiceQR() {
    let userBindAddress : UserBindDeliverAddressDTO = await firstValueFrom(this.userService.getUserBindAddress(this.user));
    let amount = this.price.toString();

    let reference : ReferenceDTO = await firstValueFrom(this.userService.getReference("invoiceReference"));
    reference.val = reference.val + 1;
    console.log("Reference: ", reference.val);
    let ref : string = reference.val.toString();
    let i = 26 - ref.length;
    let string = "";
    while (i != 0){
      string = string.concat("0");
      i--;
    }
    string = string.concat(ref);
    this.reference = string;

    await firstValueFrom(this.userService.putReference(reference));
    let myConfiguration  = {
      "Account" : "CH4431999123000889012",
      "CreditorName" : "Balmburren",
      "CreditorAddress1" : "Mengestorfbergstrasse 198",
      "CreditorAddress2" : "3144 Mengestorf",
      "CreditorCountryCode" : "CH",
      // "DebtorName" : "LivingTech GmbH",
      "DebtorName" : this.user.firstname + " " + this.user.lastname,
      // "DebtorAddress1" : "Dörflistrasse 10",
      "DebtorAddress1" : userBindAddress.address.street + " " + userBindAddress.address.number,
      // "DebtorAddress2" : "8057 Zürich",
      "DebtorAddress2" : userBindAddress.address.plz + " " + userBindAddress.address.city,
      "DebtorCountryCode" : "CH",
      // "Amount" : "1.50",
      "Amount" : amount,
      // "ReferenceNr" : "21000000000313947143000901",
      "ReferenceNr" : this.reference,
      "UnstructuredMessage" : "Mitteilung zur Rechnung",
      "Currency" : "CHF",
      "QrOnly" : "false",
      "Format" : "PDF",
      "Language" : "DE"
    }

    // Main configuration
    let myEndpointUrl = "http://v2.qrbillservice.livingtech.ch";
    let myEndpointPath = "/api/qrinvoice/create/";
    let myApiKey = "WHyxnqWfVDkc";

    let invoiceQR: InvoiceQRDTO = new InvoiceQRDTO();
    invoiceQR.baseUrl = myEndpointUrl;
    invoiceQR.createUrl = myEndpointPath;
    invoiceQR.apiKey = myApiKey;

    let myGetParams = new URLSearchParams(myConfiguration);
    invoiceQR.params = myGetParams.toString();
    await this.downloadXML(userBindAddress);
    return await firstValueFrom(this.emailService.createInvoiceQR(invoiceQR));
  }

  async sendQRInvoice() {
    let userBindPhone : UserBindPhoneDTO = await firstValueFrom(this.userService.getUserBindPhone(this.user));
    let emailData : EmailDataDTO = new EmailDataDTO();
    let json : any = JSON.parse(JSON.stringify(await this.getInvoiceQR()));
    let string: string = json["base64Content"];
    emailData.base64String = string;

    emailData.fromEmail = "balmburren@gmail.com";
    emailData.toEmail = userBindPhone.email;
    emailData.subject = "Rechnung Balmburren";
    emailData.body ="Guten Tag " + this.user.firstname +' ' + this.user.lastname + " Balmburren sendet Ihnen den Einzahlungsschein im Anhang. \n Freundliche Grüsse Balmburren";
    emailData.password = "123456";
    emailData.filename = "Balmburren_Einzahlungsschein.pdf";
    emailData.type = "attachment";

    console.log("Email string QR: " + this.emailData.base64String);
    await firstValueFrom(this.emailService.sendEmail(emailData));
  }

  private xmlService(xml: string, filename: string) {
    let pom = document.createElement('a');
    let bb = new Blob([xml], {type: 'text/plain'});
    pom.setAttribute('href', window.URL.createObjectURL(bb));
    pom.setAttribute('download', filename);
    pom.dataset['downloadurl'] = ['text/plain', pom.href].join(':');
    pom.draggable = true;
    pom.classList.add('dragout');
    pom.click();
  }

  async downloadXML(userBindAddress: UserBindDeliverAddressDTO) {
    let actualDay = new Date().toISOString().split('T')[0];
    let xmlUser = this.createUserXML(actualDay, userBindAddress);
    // let example = "<something><somethingelse></somethingelse></something>";
    this.xmlService(xmlUser, 'Import_Debi_Kunden_' + this.user.firstname + '_' + this.user.lastname + '.xml');
    let xmlInvoice = this.createInvoiceXML(actualDay);
    this.xmlService(xmlInvoice,'Import_Debi_Belege_' + this.user.firstname + '_' + this.user.lastname + '.xml');
  }

  private createUserXML(actualDay: string, userBindAddress: UserBindDeliverAddressDTO) {
    let digit: string = this.computeSwissInvoiceCheckDigit(this.reference).toString();
    let reference = this.reference.concat(digit);
    let xmlUser = "<?xml version='1.0' encoding='UTF-8'?><AbaConnectContainer><Task><Parameter><Application>DEBI</Application><Id>Kunden</Id>" +
      "<MapId>AbaDefault</MapId><Version>2018.00</Version></Parameter><Transaction><Customer mode='SAVE'>" +
      "<CustomerNumber>"+this.user.id+"</CustomerNumber><CustomerID/>" +
      "<PaymentTermNumber>1</PaymentTermNumber><DefaultCurrency>CHF</DefaultCurrency><ReminderProcedure>NORM</ReminderProcedure>" +
      "<AddressData mode='SAVE'><AddressNumber>-"+this.user.id+"</AddressNumber><Name>"+ this.user.lastname+"</Name><FirstName>"+ this.user.firstname+"</FirstName>" +
      "<AdditionalLine/>" +
      "<Country>CH</Country><ZIP>"+ userBindAddress.address.plz + "</ZIP><City>"+ userBindAddress.address.city +"</City>" +
      "<Language>D</Language><Street>"+userBindAddress.address.street+"</Street>" +
      "<HouseNumber>"+ userBindAddress.address.number+"</HouseNumber></AddressData></Customer></Transaction></Task></AbaConnectContainer>";
    return xmlUser;
  }

  private createInvoiceXML(actualDay: string) {
    let digit: string = this.computeSwissInvoiceCheckDigit(this.reference).toString();
    let reference = this.reference.concat(digit);
    let price = (Math.round(this.price * 100) / 100).toFixed(2);
    let xmlInvoice = "<?xml version='1.0' encoding='UTF-8'?><AbaConnectContainer><Task><Parameter><Application>DEBI</Application>" +
      "<Id>Belege</Id><MapId>AbaDefault</MapId><Version>2018.00</Version></Parameter><Transaction><Document mode='SAVE'><DocumentCode>R</DocumentCode>" +
      "<CustomerNumber>"+ this.user.id +"</CustomerNumber><Number></Number><Reference>Balmburren</Reference>" +
      "<Text></Text><AccountReceivableDate>"+ actualDay+"</AccountReceivableDate>" +
      "<Currency>CHF</Currency><Amount>"+ price.toString()+"</Amount><KeyAmount>"+ price.toString()+"</KeyAmount>"+
      "<PaymentOrderProcedure>1</PaymentOrderProcedure>" +
      "<PaymentReferenceLineType>QR</PaymentReferenceLineType>" +
      "<PaymentReferenceLine>"+ reference + "</PaymentReferenceLine>"+
      "<TaxRecalculation>true</TaxRecalculation>"+

      "<LineItem mode='SAVE'><Number>1</Number><Amount>"+ price.toString()+"</Amount><KeyAmount>"+ price.toString()+"</KeyAmount>" +
      "<CreditAccount>3400''''''''''''''''''''</CreditAccount><TaxMethod>1</TaxMethod><TaxCode>311</TaxCode>" +
      "<TaxIncluded>2</TaxIncluded><TaxDateValidFrom>"+actualDay+"</TaxDateValidFrom><TaxAmount></TaxAmount><KeyTaxAmount></KeyTaxAmount>" +
      "<Text></Text><CreditCostCentre1>0</CreditCostCentre1>" +
      "<AccrualNumber>0</AccrualNumber></LineItem>"+
      "</Document></Transaction></Task></AbaConnectContainer>";
    return xmlInvoice;
  }

  computeSwissInvoiceCheckDigit(invoiceNumber: string) {

    if (invoiceNumber.length !== 26) {
      throw new Error("Swiss invoice numbers should have 26 digits.");
    }

    const intTabelle = [0, 9, 4, 6, 8, 2, 7, 1, 3, 5];
    let intÜbertrag = 0;

    for (let intIndex = 0; intIndex < invoiceNumber.length; intIndex++) {
      intÜbertrag = intTabelle[(intÜbertrag + parseInt(invoiceNumber.charAt(intIndex))) % 10];
    }

    const checkDigit = (10 - intÜbertrag) % 10;
    return checkDigit;
  }
}
