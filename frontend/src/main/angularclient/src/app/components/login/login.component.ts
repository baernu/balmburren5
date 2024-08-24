import { Component } from '@angular/core';
import {UserDTO} from "../user/service/userDTO";
import {ActivatedRoute, Router, RouterModule} from "@angular/router";
import {UserService} from "../user/service/user-service.service";
import {AuthenticateDTO} from "../user/service/authenticateDTO";
import {firstValueFrom} from "rxjs";


@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  user: UserDTO;
  showPassword: boolean = false;
  authenticate: AuthenticateDTO;
  token: string = "";
  bool: boolean = false;
  error: any;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {
    this.user = new UserDTO();
    this.authenticate = new AuthenticateDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async onSubmit() {
    try{
        this.token = await firstValueFrom(this.userService.login(this.user));
    }catch(error){


      // @ts-ignore
      if (error.status === 401 || 404)
        this.error = "Username Password sind nicht korrekt."
        // await this.router.navigate(['login']);
        return;
    }
    await firstValueFrom(this.userService.setTokenCookie(this.token));
    let msg = await firstValueFrom(this.userService.createUser(this.user.username));
    console.log("Boolean add Role user: " + msg);
    await this.router.navigate(['home']);
  }


}
