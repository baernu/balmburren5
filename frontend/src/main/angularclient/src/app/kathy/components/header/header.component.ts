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

  constructor(
    private userService: UserService,
    private router: Router){}

  async ngOnInit(): Promise<void> {
    let username = localStorage.getItem('username');
    if (username)
      this.user = await firstValueFrom(this.userService.findUser(username));
  }

  change() {
    this.router.navigate(['kathy']);
  }
}

