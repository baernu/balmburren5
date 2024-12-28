import { Component } from '@angular/core';
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {firstValueFrom} from "rxjs";

@Component({
  selector: 'app-search-user',
  templateUrl: './search-user.component.html',
  styleUrls: ['./search-user.component.css']
})
export class SearchUserComponent {
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

  async userOrder(user: UserDTO) {
    let bool = await firstValueFrom(this.userService.isUserKathy(user.username));
    if (bool)
      await this.router.navigate(['kathy_users_order/'],
        {
          queryParams: {
            param1: user.username
          }
        });
    else {
      this.error = "Keine Berechtigung für diese Anzeige!";
      setTimeout(async () => {
        this.error = "";
      }, 2000);
    }
  }

  userOrdered(user: UserDTO) {
    this.router.navigate(['kathy_users_ordered/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

}
