import {Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
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
  // userBindRoles: UserBindRoleDTO[] = [];
  // userBindRole: UserBindRoleDTO = new UserBindRoleDTO();
  roles: RoleDTO[] = [];
  username:string = "";
  newRoles: RoleDTO[] =[];
  boolClicked: boolean = false;



  constructor(
    private userService: UserService,
    private route: ActivatedRoute,
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
    // this.user = user;
    console.log("User is: {}", user);
    this.roles = [];
    this.username = user.username;
    this.user = await firstValueFrom(this.userService.findUser(this.username));
    let userBindRoleDTOS = await firstValueFrom(this.userService.getAllUserBindRoles(this.username));
    userBindRoleDTOS.forEach(e => this.roles.push(e.role));
    this.user.roles = this.roles;
    // this.roles = this.user.roles;
    // this.userBindRoles = await firstValueFrom(this.userService.findAllRolesForPerson(this.username));
    // this.userBindRoles.forEach(userBindRole => this.roles.push(userBindRole.role));
  }

  async addRoles() {
    this.newRoles = await firstValueFrom(this.userService.findAllRoles());
    if (this.boolClicked) this.boolClicked = false;
    else this.boolClicked = true;
  }

  async addRole(role: RoleDTO) {
    // let userBindRole1: UserBindRoleDTO = new UserBindRoleDTO();
    // userBindRole1.role = role;
    // let user1: UserDTO = await firstValueFrom(this.userService.findUser(this.username));
    let roles: RoleDTO[] = this.user.roles;
    if (roles.some(e => e.name == role.name)) return;
    else {
      this.user.roles.push(role);
      let userBindRole = new UserBindRoleDTO();
      userBindRole.role = role;
      userBindRole.user = this.user;
      await firstValueFrom(this.userService.saveUserBindRoles(userBindRole));
      // let user1 = await firstValueFrom(this.userService.updateUser(this.user));
      // console.log("user with new roles is: " + user1);
      await this.router.navigate(['/admin_users_role']);
    }
    // userBindRole1.person = user1;
    // userBindRole1 = await firstValueFrom(this.userService.createPersonBindRole(userBindRole1));
    // this.userBindRoles = await firstValueFrom(this.userService.findAllRolesForPerson(this.username));
    // if(this.roles.find(role => role.name == userBindRole1.role.name) == null) this.roles.push(userBindRole1.role);
  }

  async delete(role: RoleDTO) {
    // let userBindRole1: UserBindRoleDTO = new UserBindRoleDTO();
    // userBindRole1 = await firstValueFrom(this.userService.deletePersonBindRole(this.username, role.name));
    // this.roles.splice(this.roles.findIndex(r => r.name === userBindRole1.role.name), 1);
    // let user1: UserDTO = await firstValueFrom(this.userService.findUser(this.username));
    // this.user.roles = this.roles.filter(r => r.name != role.name);
    // let user1 = await firstValueFrom(this.userService.updateUser(this.user));
    // console.log("user with deleted roles is: " + user1);
    let userBindRoles = await firstValueFrom(this.userService.getAllUserBindRoles(this.user.username));
    let userBindRole = userBindRoles.find(e => e.role.name == role.name);
    if(userBindRole) await firstValueFrom(this.userService.deleteUserBindRoles(userBindRole));
    await this.router.navigate(['/admin_users_role']);
  }
}

