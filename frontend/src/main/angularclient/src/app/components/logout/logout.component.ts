import { Component } from '@angular/core';
import {UserService} from "../user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import { Router } from '@angular/router';


@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  constructor(
    private router: Router,
    private userService: UserService) {

  }
  async ngOnInit() {
    await firstValueFrom(this.userService.deleteTokenCookie());
    await this.router.navigate(['home']);
  }
}
