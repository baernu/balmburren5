import { Component } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserService } from '../../../../components/user/service/user-service.service';
import { UserDTO } from '../../../../components/user/service/userDTO';
import {AuthenticateDTO} from "../../../../components/user/service/authenticateDTO";
import {firstValueFrom} from "rxjs";
import {ErrorHandlingService} from "../../../../components/error_handling/error-handling.service";

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
      await this.router.navigate(['admin']);
      return;
    }

    await this.router.navigate(['admin_users_add']);
    this.error ="Unspezifischer Fehler.";

  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
