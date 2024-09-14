import { Injectable } from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {firstValueFrom, Observable} from 'rxjs';
import {UserService} from "../components/user/service/user-service.service";
import {UserDTO} from "../components/user/service/userDTO";

@Injectable({
  providedIn: 'root'
  })
export class AuthGuardGuard implements CanActivate {
  private people: UserDTO = new UserDTO();

  constructor(private userService: UserService, private router: Router) {}

  async canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Promise<boolean> {
    let user: UserDTO = new UserDTO();
    try{
      user = await firstValueFrom(this.userService.currentUser());
    }catch(error:any){
      if(error.status !=200){
        await this.router.navigate(['home']);
        return false;
      }
    }
    this.people = user;
    try{
      await firstValueFrom(this.userService.isAdmin(this.people.username));
    }catch(error: any){
      if(error.bool != true) await this.router.navigate(['home']);
      return false;
    }

    return true;
  }
}
