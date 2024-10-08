import {Component, OnInit} from '@angular/core';
import { Router} from "@angular/router";
import {firstValueFrom} from "rxjs";
import {UserDTO} from "../../../../components/user/service/userDTO";
import {UserService} from "../../../../components/user/service/user-service.service";
import {RoleDTO} from "../../../../components/user/service/roleDTO";
import {UserBindRoleDTO} from "../../../../components/user/service/userBindRoleDTO";

@Component({
  selector: 'app-add-role',
  templateUrl: './add-role.component.html',
  styleUrls: ['./add-role.component.css']
})
export class AddRoleComponent implements OnInit{

  users: UserDTO[] =[];
  user: UserDTO = new UserDTO();
  roles: RoleDTO[] = [];
  username:string = "";
  newRoles: RoleDTO[] =[];
  boolClicked: boolean = false;



  constructor(
    private userService: UserService,
    private router: Router,
  ){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;};
  }


  async ngOnInit() {
    this.users = await firstValueFrom(this.userService.findAll());
  }

  async goTo(user: UserDTO) {
    this.newRoles = [];
    this.boolClicked = false;
    this.roles = [];
    this.username = user.username;
    this.user = await firstValueFrom(this.userService.findUser(this.username));
    let userBindRoleDTOS = await firstValueFrom(this.userService.getAllUserBindRoles(this.username));
    userBindRoleDTOS.forEach(e => this.roles.push(e.role));
    this.user.roles = this.roles;
  }

  async addRoles() {
    this.newRoles = await firstValueFrom(this.userService.findAllRoles());
    if (this.boolClicked) this.boolClicked = false;
    else this.boolClicked = true;
  }

  async addRole(role: RoleDTO) {
    let roles: RoleDTO[] = this.user.roles;
    if (roles.some(e => e.name == role.name)) return;
    else {
      this.user.roles.push(role);
      let userBindRole = new UserBindRoleDTO();
      userBindRole.role = role;
      userBindRole.user = this.user;
      await firstValueFrom(this.userService.saveUserBindRoles(userBindRole));
      await this.router.navigate(['/admin_users_role']);
    }
  }

  async delete(role: RoleDTO) {
    let userBindRoles = await firstValueFrom(this.userService.getAllUserBindRoles(this.user.username));
    let userBindRole = userBindRoles.find(e => e.role.name == role.name);
    if(userBindRole) await firstValueFrom(this.userService.deleteUserBindRoles(userBindRole));
    await this.router.navigate(['/admin_users_role']);
  }
}

