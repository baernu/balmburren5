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

  constructor(private userService: UserService,
              private router: Router) {
  }

  async ngOnInit(){
    this.users = await firstValueFrom(this.userService.findAll());
    if(this.users) {
      this.users = this.users.sort((u1: UserDTO, u2: UserDTO) => {
       if (u1.lastname && u2.lastname) return u1.lastname.localeCompare(u2.lastname)
       else return 0;
      });
      for (const user of this.users) {
        let user1: UserDTO = await firstValueFrom(this.userService.findUser(user.username));
        let userWithRole: UserWithRoleDTO = new UserWithRoleDTO();
        // let userBindRoles: UserBindRoleDTO[] = await firstValueFrom(this.userService.findAllRolesForPerson(user.username));
        let userBindRoles: UserBindRoleDTO[] = await firstValueFrom(this.userService.getAllUserBindRoles(user1.username));
        console.log("userbindroles :" + userBindRoles);
        // user1.roles.forEach(e => {
        //   let userBindRole: UserBindRoleDTO = new UserBindRoleDTO();
        //   userBindRole.role = e;
        //   userBindRole.person = user1;
        //   userBindRoles.push(userBindRole);
        // })

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
        this.usersWithRole.push(userWithRole);
      }
    }
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
}
