import { Component } from '@angular/core';
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-admin-header',
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
    // let username = localStorage.getItem("username");
    // if (username)
    //   this.user = await firstValueFrom(this.userService.findUser(username));
    this.user = await firstValueFrom(this.userService.currentUser());
    this.pathname = location.pathname;
  }

  change() {
    this.router.navigate(['admin']);
  }
}
