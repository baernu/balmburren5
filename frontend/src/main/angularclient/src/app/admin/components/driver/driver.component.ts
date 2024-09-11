import {Component, OnInit} from '@angular/core';
import {UserDTO} from "../../../components/user/service/userDTO";
import {UserWithRoleDTO} from "../../../components/user/service/userWithRoleDTO";
import {UserService} from "../../../components/user/service/user-service.service";
import {Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {UserBindRoleDTO} from "../../../components/user/service/userBindRoleDTO";

@Component({
  selector: 'app-driver',
  templateUrl: './driver.component.html',
  styleUrls: ['./driver.component.css']
})
export class DriverComponent implements OnInit {
  users: UserDTO[] = [];
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
        let userBindRoles: UserBindRoleDTO[] = [];
        user1.roles.forEach(e => {
          let userBindRole: UserBindRoleDTO = new UserBindRoleDTO();
          userBindRole.role = e;
          userBindRole.person = user1;
          userBindRoles.push(userBindRole);
        })

        userBindRoles.forEach(userBindRole => {
          if (userBindRole.role.name == "ADMIN") userWithRole.adminIsChecked = true;
          if (userBindRole.role.name == "DRIVER") userWithRole.driverIsChecked = true;
          if (userBindRole.role.name == "USER") userWithRole.userIsChecked = true;
        });
        userWithRole.username = user.username;
        userWithRole.firstname = user.firstname;
        userWithRole.lastname = user.lastname;
        if (userWithRole.driverIsChecked) this.usersWithRole.push(userWithRole);
      }
    }
  }

  driverWork(user: UserDTO) {
    this.router.navigate(['driver_work/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }

  driverOverview(user: UserDTO) {
    this.router.navigate(['driver_overview/'],
      {
        queryParams: {
          param1: user.username
        }
      });
  }


}
