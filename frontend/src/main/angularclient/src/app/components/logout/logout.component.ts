import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../user/service/user-service.service";
import {firstValueFrom} from "rxjs";


@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent {
  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private userService: UserService) {

  }
  async ngOnInit() {
      await firstValueFrom(this.userService.deleteTokenCookie());
      // localStorage.setItem('username', "");
  }
}
