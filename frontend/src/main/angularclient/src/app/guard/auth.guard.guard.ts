import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {firstValueFrom, Observable} from 'rxjs';
import {UserService} from "../components/user/service/user-service.service";
import {UserDTO} from "../components/user/service/userDTO";

@Injectable({
  providedIn: 'root'
  })
export class AuthGuardGuard implements CanActivate {
  private message: Boolean = false;
  private bool: boolean = false;
  private people: UserDTO = new UserDTO();

  constructor(private userService: UserService, private router: Router) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    let user: UserDTO = new UserDTO();
    let username = localStorage.getItem('username');
    if (username && username != "") {
      user = await firstValueFrom(this.userService.findUser(username));
    }
    if ( username == "") {
      await this.router.navigate(['home']);
      return false;
    }
    this.people = user;
    this.message = await firstValueFrom(this.userService.isAdmin(this.people.username));
    this.bool = this.message.valueOf();
    if (!this.bool) {
      await this.router.navigate(['home']);
      return this.bool;
    }
    return true;

  }
}
