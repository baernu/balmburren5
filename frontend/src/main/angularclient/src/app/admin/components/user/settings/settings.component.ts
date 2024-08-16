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
    this.user = await firstValueFrom(this.userService.updateUser(this.user));
    this.user.password = "";
    this.userBindPhone.person = await firstValueFrom(this.userService.findUser(this.user.username));
    this.userBindAddress.person = this.userBindPhone.person;
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
    await this.router.navigate(['admin_users_settings/'],
      {
        queryParams: {
          param1: this.user.username
        }
      });
  }
  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async savePassword() {
    this.user = await firstValueFrom(this.userService.updateUser(this.user));
  }
}
