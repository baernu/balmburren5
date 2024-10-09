import { Component } from '@angular/core';
import {UserService} from "../user/service/user-service.service";
import {Router} from "@angular/router";
import {UserDTO} from "../user/service/userDTO";
import {firstValueFrom} from "rxjs";
import {EmailDataDTO} from "../../admin/components/email/email-service/EmailDataDTO";
import {UserBindPhoneDTO} from "../user/service/UserBindPhoneDTO";
import {EmailService} from "../../admin/components/email/email-service/email.service";
import {UserBindDeliverAddressDTO} from "../user/service/userBindDeliverAddressDTO";
import {AddressDTO} from "../user/service/addressDTO";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  user: UserDTO;
  userBindPhone: UserBindPhoneDTO = new UserBindPhoneDTO();
  userBindAddress: UserBindDeliverAddressDTO = new UserBindDeliverAddressDTO();
  showPassword: boolean = false;
  error: string = "";
  success: string = "";

  constructor(
    private router: Router,
    private userService: UserService,
    private emailService: EmailService,
    ) {
    this.user = new UserDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }


  async onSubmit() {

    let bool = await firstValueFrom(this.userService.existUser(this.user.username));


    if (bool) {
      this.error = "Username ist besetzt, bitte neu bestimmen.";
      setTimeout( () => {
        this.success = "";
        this.error = "";
      }, 2000);
      return;
    }
    if (this.user.password.length < 8) {
      this.error = "Passwort ist zu kurz.";
      setTimeout( () => {
        this.success = "";
        this.error = "";
      }, 2000);
      return;
    }

    if (!bool && this.user.password.length > 7) {
      try {
        this.userBindPhone.user  = await firstValueFrom(this.userService.register(this.user));
        this.userBindPhone.invoicePerson = this.userBindPhone.user;
        await firstValueFrom(this.userService.createUserBindPhone(this.userBindPhone));
      }catch(error: any){
        if(error.status != 200 || 201) {
          this.error = "Registrierung  Email/ Phone hat nicht geklappt!";
          setTimeout( () => {
            this.success = "";
            this.error = "";
          }, 2000);
          return;
        }
      }
      // try {
      //   this.userBindAddress.address = await firstValueFrom(this.userService.createAddress(this.userBindAddress.address));
      //   this.userBindAddress.user = this.userBindPhone.user;
      //   this.userBindAddress = await firstValueFrom(this.userService.createUserBindAddress(this.userBindAddress));
      // }catch(error: any){
      //   if(error.status != 200 || 201){
      //     this.error = "Registrierung Adresse hat nicht geklappt!";
      //     setTimeout( () => {
      //       this.success = "";
      //       this.error = "";
      //     }, 2000);
      //     return;
      //   }
      // }
      this.success = "Bitte schauen sie in den nächsten Minuten in Ihrem Email Postfach.";
      this.sendRegisterMessage();
    }
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async sendRegisterMessage() {
    try{
      // let emailData = new EmailDataDTO();;
      // emailData.type = "normal";
      // emailData.body = "Guten Tag\nSie haben sich neu bei Balmburren registriert. Bitte antworten Sie auf diese Mail und bestätigen kurz,\nbei Balmburren als Kunde * in online bestellen zu wollen.\nVielen Dank.";
      // emailData.toEmail = this.userBindPhone.email;
      // emailData.subject = "Registrierung Balmburren";
      await firstValueFrom(this.emailService.sendRegisterEmail(this.userBindPhone.email));
    }catch(error:any){
      if(error.status != 200) {
        this.error = "Registrierungsmail konnte nicht gesendet werden!";
        setTimeout(() => {
          this.success = "";
          this.error = "";
        }, 2000);
        return;
      }
    }
    this.success = "Registrierungsmail wurde gesendet. Bitte lesen Sie die Mail. Sie werden zur Hauptseite weitergleitet."
    setTimeout( () => {
      this.success = "";
    }, 8000);
  }

}
