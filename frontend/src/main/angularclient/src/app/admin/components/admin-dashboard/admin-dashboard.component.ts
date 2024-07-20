import { Component } from '@angular/core';
import {ActivatedRoute, Router} from "@angular/router";
import {UserService} from "../../../components/user/service/user-service.service";
import {ErrorHandlingService} from "../../../components/error_handling/error-handling.service";
import {UserDTO} from "../../../components/user/service/userDTO";

@Component({
  selector: 'app-admin-dashboard',
  templateUrl: './admin-dashboard.component.html',
  styleUrls: ['./admin-dashboard.component.css']
})
export class AdminDashboardComponent {

  constructor(
    private router: Router,
  ) {
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }
}
