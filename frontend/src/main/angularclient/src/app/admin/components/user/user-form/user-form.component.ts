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

  async ngOnInit(): Promise<void> {
    let bool1: Boolean = await firstValueFrom(this.errorHandlingService.getBoolRegister1());
    let bool2: Boolean = await firstValueFrom(this.errorHandlingService.getBoolRegister2());
    this.bool1 = bool1.valueOf();
    this.bool2 = bool2.valueOf();
  }

  async onSubmit() {
    if (!await firstValueFrom(this.userService.existUser(this.user.username))
      && this.user.password.length > 7) {
      this.user = await firstValueFrom(this.userService.save(this.user));
      await firstValueFrom(this.errorHandlingService.putBoolRegister1(false));
      await firstValueFrom(this.errorHandlingService.putBoolRegister2(false));
      await this.router.navigate(['admin']);
      return;
    }
    if (await firstValueFrom(this.userService.existUser(this.user.username))) {
      await firstValueFrom(this.errorHandlingService.putBoolRegister1(true));
    } else await firstValueFrom(this.errorHandlingService.putBoolRegister1(false));
    if (this.user.password.length < 8)
      await firstValueFrom(this.errorHandlingService.putBoolRegister2(true));
    else await firstValueFrom(this.errorHandlingService.putBoolRegister2(false));
    await this.router.navigate(['admin_users_add']);
  }

  showHidePassword() {
    this.showPassword = !this.showPassword;
  }
}
