import { Component } from '@angular/core';
import {UserService} from "../user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserDTO} from "../user/service/userDTO";
import {UserBindPhoneDTO} from "../user/service/UserBindPhoneDTO";
import {firstValueFrom} from "rxjs";
import {ErrorHandlingService} from "../error_handling/error-handling.service";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  user: UserDTO;
  showPassword: boolean = false;
  error: any;

  constructor(
    private route: ActivatedRoute,
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
      return;
    }
    if (this.user.password.length < 8) {
      this.error = "Passswort ist zu kurz.";
      return;
    }

    if (!bool && this.user.password.length > 7) {
      this.user = await firstValueFrom(this.userService.register(this.user));
      await this.router.navigate(['login']);
      return;
    }

    await this.router.navigate(['register']);
    this.error ="Unspezifischer Fehler.";
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
