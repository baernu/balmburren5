import { Component } from '@angular/core';
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-search-user-admin',
  templateUrl: './search-user-admin.component.html',
  styleUrls: ['./search-user-admin.component.css']
})
export class SearchUserAdminComponent {
  user: UserDTO = new UserDTO();
  success: string = "";
  error: string = "";

  constructor(private userService: UserService,
              private router: Router,
              private route: ActivatedRoute) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;}
  }

  async ngOnInit(){
    if (this.user.username != "") this.user = await firstValueFrom(this.userService.findUser(this.user.username));
    if (this.user.username == "")this.error = "User nicht gefunden!";
    else this.success = "User gefunden!";
    setTimeout(() => {
      this.error = "";
      this.success = "";}, 1000);
  }

  userOrder(user: UserDTO) {
    this.router.navigate(['admin_users_order/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

  userOrdered(user: UserDTO) {
    this.router.navigate(['admin_users_ordered/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }


  userSettings(user: UserDTO) {
    this.router.navigate(['admin_users_settings/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }
}
