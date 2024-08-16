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
  bool1: boolean = false;
  bool2: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private errorHandlingService: ErrorHandlingService,
    ) {
    this.user = new UserDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  // async ngOnInit(): Promise<void> {
  //   let bool1: Boolean = await firstValueFrom(this.errorHandlingService.getBoolRegister1());
  //   let bool2: Boolean = await firstValueFrom(this.errorHandlingService.getBoolRegister2());
  //   this.bool1 = bool1.valueOf();
  //   this.bool2 = bool2.valueOf();
  // }

  async onSubmit() {
    console.log("exist user: " + await firstValueFrom(this.userService.existUser(this.user.username)));
    if (!await firstValueFrom(this.userService.existUser(this.user.username))
    && this.user.password.length > 7) {
      this.user = await firstValueFrom(this.userService.register(this.user));
      // await firstValueFrom(this.errorHandlingService.putBoolRegister1(false));
      // await firstValueFrom(this.errorHandlingService.putBoolRegister2(false));
      await this.router.navigate(['login']);
      return;
    }
    // if (await firstValueFrom(this.userService.existUser(this.user.username))) {
    //   await firstValueFrom(this.errorHandlingService.putBoolRegister1(true));
    // } else await firstValueFrom(this.errorHandlingService.putBoolRegister1(false));
    // if (this.user.password.length < 8)
    //   await firstValueFrom(this.errorHandlingService.putBoolRegister2(true));
    // else await firstValueFrom(this.errorHandlingService.putBoolRegister2(false));
    await this.router.navigate(['register']);
    console.log("schade...");
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
