import { Component } from '@angular/core';
import {UserDTO} from "../user/service/userDTO";
import {ActivatedRoute, Router, RouterModule} from "@angular/router";
import {UserService} from "../user/service/user-service.service";
import {AuthenticateDTO} from "../user/service/authenticateDTO";
import {firstValueFrom} from "rxjs";
import {ErrorHandlingService} from "../error_handling/error-handling.service";


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

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private errorHandlingService: ErrorHandlingService) {
    this.user = new UserDTO();
    this.authenticate = new AuthenticateDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  // async ngOnInit(): Promise<void> {
  //   let bool: Boolean = await firstValueFrom(this.errorHandlingService.getBoolLogin());
  //   this.bool = bool.valueOf();
  // }

  async onSubmit() {
    try{
        this.token = await firstValueFrom(this.userService.login(this.user));
    }catch(error){
        // await firstValueFrom(this.errorHandlingService.putBoolLogin(true));
        await this.router.navigate(['login']);
        return;
    }
    // this.authenticate.username = this.user.username;
    // this.authenticate.password = this.user.password;
    // localStorage.setItem("username", this.user.username);
    // await firstValueFrom(this.userService.authenticate(this.authenticate));
    await firstValueFrom(this.userService.setTokenCookie(this.token));
    let msg = await firstValueFrom(this.userService.createUser(this.user.username));
    console.log("Boolean add Role user: " + msg);
    // await firstValueFrom(this.errorHandlingService.putBoolLogin(false));
    await this.router.navigate(['home']);
  }


}
