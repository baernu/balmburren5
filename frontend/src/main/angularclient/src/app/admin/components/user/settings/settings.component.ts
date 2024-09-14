import { Component } from '@angular/core';
import {firstValueFrom} from "rxjs";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserBindPhoneDTO} from "../../../../components/user/service/UserBindPhoneDTO";
import {UserBindDeliverAddressDTO} from "../../../../components/user/service/userBindDeliverAddressDTO";

@Component({
  selector: 'app-settings',
  templateUrl: './settings.component.html',
  styleUrls: ['./settings.component.css']
})
export class SettingsComponent {
  showPassword: boolean = false;
  param1: string | null = "";
  user: UserDTO = new UserDTO();
  userBindPhone: UserBindPhoneDTO = new UserBindPhoneDTO();
  userBindAddress: UserBindDeliverAddressDTO = new UserBindDeliverAddressDTO();
  hash: string = "";
  error: string = "";
  success: string = "";

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(): Promise<void> {

    this.param1= this.route.snapshot.queryParamMap.get('param1');
    if (this.param1 != null) this.user = await firstValueFrom(this.userService.findUser(this.param1));
    this.hash = this.user.password;
    this.user.password = "";
    if(await firstValueFrom(this.userService.existUserBindPhone(this.user)))
      this.userBindPhone = await firstValueFrom(this.userService.getUserBindPhone(this.user));
    if(await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
      this.userBindAddress = await firstValueFrom(this.userService.getUserBindAddress(this.user));
      if (this.userBindPhone.invoicePerson.username != null) return;
    }
    this.userBindPhone.invoicePerson.username = this.user.username;
  }

  async onSubmit() {
    this.user.password = this.hash;
    try {
      this.user = await firstValueFrom(this.userService.updateUser(this.user));
    }catch(error: any) {
      if(error.status != 200) {
        this.error = "Update User hat nicht geklappt!";
        setTimeout(async () => {
          this.success = "";
          this.error = "";
          return;
        }, 1000);
      }
    }
    this.user.password = "";
    this.userBindPhone.user= await firstValueFrom(this.userService.findUser(this.user.username));
    this.userBindAddress.user = this.userBindPhone.user;

    try {
      if (!await firstValueFrom(this.userService.existUserBindAddress(this.user))) {
        this.userBindAddress.address = await firstValueFrom(this.userService.createAddress(this.userBindAddress.address));
        this.userBindAddress = await firstValueFrom(this.userService.createUserBindAddress(this.userBindAddress));
      } else {
        this.userBindAddress.address = await firstValueFrom(this.userService.putAddress(this.userBindAddress.address));
        this.userBindAddress = await firstValueFrom(this.userService.putUserBindAddress(this.userBindAddress));

      }
      if (!await firstValueFrom(this.userService.existUserBindPhone(this.user))) {
        this.userBindPhone.invoicePerson = await firstValueFrom(this.userService.findUser(this.userBindPhone.invoicePerson.username));
        this.userBindPhone = await firstValueFrom(this.userService.createUserBindPhone(this.userBindPhone));
      } else {
        this.userBindPhone.invoicePerson = await firstValueFrom(this.userService.findUser(this.userBindPhone.invoicePerson.username));
        this.userBindPhone = await firstValueFrom(this.userService.putUserBindPhone(this.userBindPhone));
      }
    }catch(error: any){
      if(error.status != 200) {
        this.error = "Update Adresse/ Telefon hat nicht geklappt!";
        setTimeout(async () => {
          this.success = "";
          this.error = "";
          return;
        }, 1000);
      }
    }
    this.success = "Die Daten wurden gespeichert";
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      await this.router.navigate(['admin_users_settings/'],
        {
          queryParams: {
            param1: this.user.username
          }
        });
    }, 1000);

  }
  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async savePassword() {
    try {
      await firstValueFrom(this.userService.update1User(this.user));
    }catch(error: any){
      if(error.status != 200) {
        this.error = "Passwort wurde nicht gespeichert!!";
        setTimeout(async () => {
          this.success = "";
          this.error = "";
          return;
        }, 2000);
      }
    }
    this.success = "Passwort wurde gespeichert."
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      return;
    }, 1000);

  }
}
