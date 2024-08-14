import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {firstValueFrom, Observable} from 'rxjs';
import {UserDTO} from "../../components/user/service/userDTO";
import {UserService} from "../../components/user/service/user-service.service";

@Injectable({
  providedIn: 'root'
})
export class KathyGuard implements CanActivate {
  private message: Boolean = false;
  private bool: boolean = false;
  private people: UserDTO = new UserDTO();

  constructor(private userService: UserService, private router: Router) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    let user: UserDTO = new UserDTO();
    // let username = localStorage.getItem('username');
    // if (username && username != "") {
    user = await firstValueFrom(this.userService.currentUser());
    // }
    if ( user.username == "") {
      await this.router.navigate(['home']);
      return false;
    }
    this.people = user;
    this.message = await firstValueFrom(this.userService.isKathy(this.people.username));
    this.bool = this.message.valueOf();
    if (!this.bool) {
      await this.router.navigate(['home']);
      return this.bool;
    }
    return true;

  }
}
