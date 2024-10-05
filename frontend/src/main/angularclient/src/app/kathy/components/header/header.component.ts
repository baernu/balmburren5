import { Component } from '@angular/core';
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {Router} from "@angular/router";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-kathy-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {
  user: UserDTO = new UserDTO();
  pathname: string = "";
  constructor(
    private userService: UserService,
    private router: Router){}

  async ngOnInit(): Promise<void> {
    let user = await firstValueFrom(this.userService.currentUser());
    if (user.username)
      this.user = user;
    this.pathname = location.pathname;
  }

  // change() {
  //   this.router.navigate(['kathy']);
  // }
}

