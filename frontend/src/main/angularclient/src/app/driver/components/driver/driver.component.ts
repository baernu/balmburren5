import { Component } from '@angular/core';
import {Router} from "@angular/router";

@Component({
  selector: 'app-driver',
  templateUrl: './driver.component.html',
  styleUrls: ['./driver.component.css']
})
export class DriverComponent {

  constructor(
    private router: Router){
    this.router.routeReuseStrategy.shouldReuseRoute = () => {
      return false;
    };
  }
}
