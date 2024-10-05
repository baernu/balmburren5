import { Component } from '@angular/core';
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {UserService} from "../user/service/user-service.service";
import {UserDTO} from "../user/service/userDTO";
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent {

  user: UserDTO = new UserDTO();

  constructor(
    private userService: UserService,
    private router: Router){}

  async ngOnInit(): Promise<void> {
    let user = await firstValueFrom(this.userService.currentUser());
    if (user.username)
      this.user = user;
  }

  change() {
    this.router.navigate(['home']);
  }
}
