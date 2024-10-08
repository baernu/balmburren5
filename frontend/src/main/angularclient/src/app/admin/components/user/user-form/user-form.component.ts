import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { UserService } from '../../../../components/user/service/user-service.service';
import { UserDTO } from '../../../../components/user/service/userDTO';
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-user-form',
  templateUrl: './user-form.component.html',
  styleUrls: ['./user-form.component.css']
})
export class UserFormComponent {

  user: UserDTO;
  showPassword: boolean = false;
  error: any;

  constructor(
    private router: Router,
    private userService: UserService,
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
        this.error = "";
      }, 2000);
      return;
    }
    if (this.user.password.length < 8) {
      this.error = "Passswort ist zu kurz.";
      setTimeout( () => {
        this.error = "";
      }, 2000);
      return;
    }

    if (!bool && this.user.password.length > 7) {
      this.user = await firstValueFrom(this.userService.register(this.user));
      await this.router.navigate(['admin']);
      return;
    }


    this.error ="Unspezifischer Fehler.";
    setTimeout( () => {
      this.error = "";
    }, 2000);
    await this.router.navigate(['admin_users_add']);
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
