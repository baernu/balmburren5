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
  error1: string = "";
  success1: string = "";
  showPassword: boolean = false;

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
      this.user.password = "";
    if(await firstValueFrom(this.userService.existUserBindPhone(this.user)))
      this.userBindPhone = await firstValueFrom(this.userService.getUserBindPhone(this.user));
    if(await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
      this.userBindAddress = await firstValueFrom(this.userService.getUserBindAddress(this.user));
      this.address = this.userBindAddress.address;
    }

  }

  async onSubmit() {
    if(!await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
      try {
        this.userBindAddress.address = await firstValueFrom(this.userService.createAddress(this.address));
        this.userBindAddress.user = this.user;
        this.userBindAddress = await firstValueFrom(this.userService.createUserBindAddress(this.userBindAddress));
      }catch(error: any) {
        if (error.status != 200){
          this.error = "Adresse konnte nicht gespeichert werden!";
        }
      }
      this.success = "Adresse wurde gespeichert."
      setTimeout(async () => {
        this.error = "";
        this.success = "";
        return;
      }, 1000);

    }
    else {
      try {
        this.userBindAddress.address = await firstValueFrom(this.userService.putAddress(this.address));
        this.userBindAddress = await firstValueFrom(this.userService.putUserBindAddress(this.userBindAddress));
      }catch(error: any) {
        if (error.status != 200){
          this.error = "Adresse konnte nicht gespeichert werden!";
        }
      }
      this.success = "Adresse wurde gespeichert."
      setTimeout(async () => {
        this.error = "";
        this.success = "";
        return;
      }, 1000);
    }
    if(!await firstValueFrom(this.userService.existUserBindPhone(this.user))) {
      try {
        this.userBindPhone.user = this.user;
        this.userBindPhone.invoicePerson = this.user;
        this.userBindPhone = await firstValueFrom(this.userService.createUserBindPhone(this.userBindPhone));
      }catch(error:any) {
        if(error.staus != 200)this.error = "Telefonnummer konnte nicht kreiert werden!"
      }

    } else {
      try {
        this.userBindPhone = await firstValueFrom(this.userService.putUserBindPhone(this.userBindPhone));
      }catch(error:any){
        if(error.staus != 200)this.error = "Telefonnummer konnte nicht gespeichert werden!"
      }

    }
    this.success = "Daten wurden gespeichert.";
    setTimeout(async () => {
      this.error = "";
      this.success = "";
      await this.router.navigate(['settings']);
    }, 1000);

  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async onSubmit2() {
    if (this.user.password.length > 7){
      try {
        await firstValueFrom(this.userService.update1User(this.user));
      }catch(error: any) {
        if(error.status != 200)this.error1 = "Passwort konnte nicht gespeichert werden!";
      }
      this.success = "Passwort wurde gespeichert.";
    } else {
      this.error1 = "Passwort ist zu kurz!";
    }
    setTimeout(async () => {
      this.error1 = "";
      this.success1 = "";
      await this.router.navigate(['settings']);
    }, 1000);


  }
}
