import { Component } from '@angular/core';
import {UserService} from "../user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {UserDTO} from "../user/service/userDTO";
import {firstValueFrom} from "rxjs";


@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent {

  user: UserDTO;
  showPassword: boolean = false;
  error: string = "";
  success: string = "";

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
      setTimeout(async () => {
        this.success = "";
        this.error = "";
        return;
      }, 2000);
    }
    if (this.user.password.length < 8) {
      this.error = "Passswort ist zu kurz.";
      setTimeout(async () => {
        this.success = "";
        this.error = "";
        return;
      }, 2000);
    }

    if (!bool && this.user.password.length > 7) {
      try {
        await firstValueFrom(this.userService.register(this.user));
      }catch(error: any){
        if(error.status != 200) {
          this.error = "Registrierung hat nicht geklappt!";
          setTimeout(async () => {
            this.success = "";
            this.error = "";
            return;
          }, 2000);
        }
      }
      this.success = "Registrierung hat geklappt. Sie können sich einloggen.";
      setTimeout(async () => {
        this.success = "";
        this.error = "";
        await this.router.navigate(['login']);
      }, 2000);
    }
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
