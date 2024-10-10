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
  error: string = "";
  success: string = "";

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
      try{
        this.user = await firstValueFrom(this.userService.register(this.user));
      }catch(error: any){
        if(error.status !== 200){
          this.error = "Registrieren hat nicht geklappt!";
          setTimeout( () => {
            this.error = "";
          }, 2000);
          return;
        }
      }
      this.success = "Registrierung hat geklappt.";
      setTimeout( async () => {
        this.success = "";
        await this.router.navigate(['/admin_users']);
      }, 2000);
      return;
    }
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
