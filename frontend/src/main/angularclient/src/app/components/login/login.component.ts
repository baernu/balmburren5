import { Component } from '@angular/core';
import {UserDTO} from "../user/service/userDTO";
import {ActivatedRoute, Router, RouterModule} from "@angular/router";
import {UserService} from "../user/service/user-service.service";
import {AuthenticateDTO} from "../user/service/authenticateDTO";
import {firstValueFrom} from "rxjs";
import {EmailDataDTO} from "../../admin/components/email/email-service/EmailDataDTO";
import {UserBindPhoneDTO} from "../user/service/UserBindPhoneDTO";
import {EmailService} from "../../admin/components/email/email-service/email.service";


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
  error: string = "" ;
  success: string = "";

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService,
    private emailService: EmailService) {
    this.user = new UserDTO();
    this.authenticate = new AuthenticateDTO();
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async onSubmit() {
    try{
        this.token = await firstValueFrom(this.userService.login(this.user));
    }catch(error:any){
      if (error.status === 401 || 404){
        this.error = "Username Passwort sind nicht korrekt."
        setTimeout(() => {
          this.success = "";
          this.error = "";
          return;
        }, 1000);
      }
    }
    this.success = "Login passt, sie sind angemeldet."
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      await firstValueFrom(this.userService.setTokenCookie(this.token));
      let msg = await firstValueFrom(this.userService.createUser(this.user.username));
      await this.router.navigate(['home']);

    }, 1000);

  }
  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async sendPassword() {
    this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    let password = this.randomString(10, 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
    this.user.password = password;
    try{
      this.user = await firstValueFrom(this.userService.update1User(this.user));
      let emailData = new EmailDataDTO();
      let personbindphone: UserBindPhoneDTO = await firstValueFrom(this.userService.getUserBindPhone(this.user));
      emailData.type = "normal";
      emailData.body = "Guten Tag \n  Balmburren sendet Ihnen ein neues Passwort. Bitte setzten Sie nach dem Login mit diesem Passwort ein neues Passwort bei Balmburren unter Einstellungen.\n Vielen Dank. \n Passwort: " + password;
      emailData.toEmail = personbindphone.email;
      emailData.subject = "Passwort Balmburren";
      await firstValueFrom(this.emailService.sendEmail(emailData));
    }catch(error:any){
      if(error.status != 200) {
        this.error = "Passwort konnte nicht gesendet werden!";
        setTimeout(() => {
          this.success = "";
          this.error = "";
          return;
        }, 1000);
      }
    }
    this.success = "Passwort wurde gesendet. Bitte schauen Sie in Ihrem Email Postfach nach."
    setTimeout(() => {
      this.success = "";
      this.error = "";
      return;
    }, 1000);
  }

  randomString(length:number, chars:string) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  }

}
