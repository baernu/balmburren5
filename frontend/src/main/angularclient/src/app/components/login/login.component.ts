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
        console.log("User is: " + this.user);
    }catch(error:any) {
      if (error.status === 403){
        this.error = "Keine Berechtigung: Sie sind nicht aktiv als User."
        setTimeout( () => {
          this.success = "";
          this.error = "";
        }, 2000);
        return;
      }
      if (error.status === 401 || 404){
        this.error = "Username Passwort sind nicht korrekt."
        setTimeout(() => {
          this.success = "";
          this.error = "";
        }, 2000);
        return;
      }
    }
      this.success = "Login passt, sie sind angemeldet."
      setTimeout(async () => {
        await firstValueFrom(this.userService.setTokenCookie(this.token));
        let roles = await firstValueFrom(this.userService.getAllUserBindRolesMe());
        let admin = roles.find(e => e.role.name == "ADMIN");
        if (admin) if (await firstValueFrom(this.userService.isAdmin(this.user.username))) {
          await this.router.navigate(['/admin']);
          return;
        }
        let driver = roles.find(e => e.role.name == "DRIVER")
        if (driver) if (await firstValueFrom(this.userService.isDriver(this.user.username))) {
          await this.router.navigate(['/driver']);
          return;
        }
        let kathy = roles.find(e => e.role.name == "KATHY");
        if (kathy) if (await firstValueFrom(this.userService.isKathy(this.user.username))) {
          await this.router.navigate(['/kathy']);
          return;
        }
        let user = roles.find(e => e.role.name == "USER");
        if (user) if (await firstValueFrom(this.userService.isBasic(this.user.username))) await this.router.navigate(['home']);
        return;

      }, 1000);
  }
  showHidePassword() {
    this.showPassword = !this.showPassword;
  }

  async sendPassword() {
    let user = await firstValueFrom(this.userService.findUser(this.user.username));
    let password = this.randomString(10, 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ');
    user.password = password;
    try{
      user = await firstValueFrom(this.userService.update1User(user));
      let emailData = new EmailDataDTO();
      let personbindphone: UserBindPhoneDTO = await firstValueFrom(this.userService.getUserBindPhone(user));
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
        }, 1000);
        return;
      }
    }
    this.success = "Passwort wurde gesendet. Bitte schauen Sie in Ihrem Email Postfach nach."
    setTimeout(() => {
      this.success = "";
      this.error = "";
    }, 3000);
    return;
  }

  randomString(length:number, chars:string) {
    var result = '';
    for (var i = length; i > 0; --i) result += chars[Math.floor(Math.random() * chars.length)];
    return result;
  }

  async register(){
    this.success = "Sie werden zum Registrieren weitergeleitet."
    setTimeout(async () => {
      this.success = "";
      this.error = "";
      await this.router.navigate(['register']);
    }, 2000);
  }

}
