import { Component } from '@angular/core';
import {UserBindPhoneDTO} from "../user/service/UserBindPhoneDTO";
import {AddressDTO} from "../user/service/addressDTO";
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../user/service/user-service.service";
import {ErrorHandlingService} from "../error_handling/error-handling.service";
import {UserDTO} from "../user/service/userDTO";
import {firstValueFrom} from "rxjs";
import {UserBindDeliverAddressDTO} from "../user/service/userBindDeliverAddressDTO";
import {EmailService} from "../../admin/components/email/email-service/email.service";
import {EmailDataDTO} from "../../admin/components/email/email-service/EmailDataDTO";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {
  userBindPhone: UserBindPhoneDTO = new UserBindPhoneDTO();
  userBindAddress: UserBindDeliverAddressDTO = new UserBindDeliverAddressDTO();
  address: AddressDTO = new AddressDTO();
  user: UserDTO = new UserDTO();
  emailData: EmailDataDTO = new EmailDataDTO();
  error: string = "";
  success: string = "";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private errorHandlingService: ErrorHandlingService,
    private emailService: EmailService,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {

      this.user = await firstValueFrom(this.userService.currentUser());
      this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    if(await firstValueFrom(this.userService.existUserBindPhone(this.user)))
      this.userBindPhone = await firstValueFrom(this.userService.getUserBindPhone(this.user));
    if(await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
      this.userBindAddress = await firstValueFrom(this.userService.getUserBindAddress(this.user));
      this.address = this.userBindAddress.address;
    }

  }

  async onSubmit() {
    if(!await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
      this.userBindAddress.address = await firstValueFrom(this.userService.createAddress(this.address));
      this.userBindAddress.user = this.user;
      this.userBindAddress = await firstValueFrom(this.userService.createUserBindAddress(this.userBindAddress));
    } else {
      this.userBindAddress.address = await firstValueFrom(this.userService.putAddress(this.address));

      this.userBindAddress = await firstValueFrom(this.userService.putUserBindAddress(this.userBindAddress));
    }
    if(!await firstValueFrom(this.userService.existUserBindPhone(this.user))) {
      this.userBindPhone.user = this.user;
      this.userBindPhone.invoicePerson = this.user;
      this.userBindPhone = await firstValueFrom(this.userService.createUserBindPhone(this.userBindPhone));
    } else {
      this.userBindPhone = await firstValueFrom(this.userService.putUserBindPhone(this.userBindPhone));
    }
    this.setEmailData(this.userBindPhone.email, "");
    await firstValueFrom(this.emailService.sendEmail(this.emailData));
    this.setEmailData("admin@balmburren.net", this.userBindPhone.user.firstname + ' ' + this.userBindPhone.user.lastname);
    try{
      await firstValueFrom(this.emailService.sendEmail(this.emailData));
    }catch(error: any) {
      if (error.status != 200){
        this.error = "Mail konnte nicht gesendet werden!";
      }
    }
    this.success = "Mail wurde gesendet.";
    setTimeout(async () => {
      this.error = "";
      this.success = "";
      await this.router.navigate(['settings']);
    }, 1000);

  }
  setEmailData(mail: string, message: string){
    this.emailData.subject = "Balmburren Registration " + message;
    this.emailData.body = "Guten Tag    Vielen Dank, wir werden Sie informieren sobald wir Sie als Balmburren User dazugefügt haben, damit Sie dann online bestellen können. Falls Sie schon Balmburren User sind, bekommen Sie diese Mail, weil Sie Ihre Einstellungen angepasst haben. Und bitte melden Sie Balmburren, wenn Sie eine neue Lieferadresse haben. Vielen Dank.";
    this.emailData.toEmail = mail;
    this.emailData.type = "normal";
  }
}
