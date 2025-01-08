import { Component, OnInit } from '@angular/core';
import { UserDTO } from '../../../../components/user/service/userDTO';
import { UserService } from '../../../../components/user/service/user-service.service';
import {firstValueFrom} from "rxjs";
import {Router} from "@angular/router";
import {UserWithRoleDTO} from "../../../../components/user/service/userWithRoleDTO";
import {UserBindRoleDTO} from "../../../../components/user/service/userBindRoleDTO";

@Component({
  selector: 'app-user-list',
  templateUrl: './user-list.component.html',
  styleUrls: ['./user-list.component.css']
})
export class UserListComponent implements OnInit {

  users: UserDTO[] | undefined;
  usersWithRole: UserWithRoleDTO[] = [];
  error: string = "";
  success: string = "";
  counter: number = 0;
  spinner: boolean = false;

  constructor(private userService: UserService,
              private router: Router) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }

  async ngOnInit(){
    this.spinner = true;
    this.users = await firstValueFrom(this.userService.findAll());
    if(this.users) {
      this.users = this.users.sort((u1: UserDTO, u2: UserDTO) => {
       if (u1.lastname && u2.lastname) return u1.lastname.localeCompare(u2.lastname)
       else return 0;
      });
      for (const user of this.users) {
        let user1: UserDTO = await firstValueFrom(this.userService.findUser(user.username));
        let userWithRole: UserWithRoleDTO = new UserWithRoleDTO();
        let userBindRoles: UserBindRoleDTO[] = await firstValueFrom(this.userService.getAllUserBindRoles(user1.username));


        userBindRoles.forEach(userBindRole => {
          if (userBindRole.role.name == "ADMIN") userWithRole.adminIsChecked = true;
          if (userBindRole.role.name == "DRIVER") userWithRole.driverIsChecked = true;
          if (userBindRole.role.name == "USER") userWithRole.userIsChecked = true;
          if (userBindRole.role.name == "USER_KATHY") userWithRole.userKathyIsChecked = true;
          if (userBindRole.role.name == "KATHY") userWithRole.kathyIsChecked = true;
          if (userBindRole.role.name == "SUPER_ADMIN") userWithRole.superadminIsChecked= true;
        });
        userWithRole.username = user.username;
        userWithRole.firstname = user.firstname;
        userWithRole.lastname = user.lastname;
        userWithRole.id = user.id;
        userWithRole.enabled = user.enabled;
        this.usersWithRole.push(userWithRole);
      }
    }
    this.spinner = false;
  }

  userSettings(user: UserDTO) {
    this.router.navigate(['admin_users_settings/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

  userOrder(user: UserWithRoleDTO) {
    this.router.navigate(['admin_users_order/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

  userOrdered(user: UserWithRoleDTO) {
    this.router.navigate(['admin_users_ordered/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

  async deleteUser(user: UserDTO) {
    this.counter++;
    if (this.counter == 7){
      try {
        await firstValueFrom(this.userService.deleteUser(user));
      }catch(error: any) {
        if (error.status != 200) {
          this.error = "Löschen User hat nicht geklappt! Bitte User in Einstellungen checken.";
          setTimeout(() => {
            this.error = "";
          }, 1000);
          this.counter = 0;
          return;
        }
      }
      this.success = "Löschen User hat geklappt";
      setTimeout( async() => {
        this.success = "";
      }, 2000);
      this.counter = 0;
      await this.router.navigate(['/admin_users']);
      return;
    } else {
      this.error = "Klicke noch " + (7 - this.counter) + " mal um zu löschen";
      setTimeout(() => {
        this.error = "";
      }, 1000);
    }
  }
}
