import { Component } from '@angular/core';
import {UserService} from "../user/service/user-service.service";
import {ActivatedRoute, Router} from "@angular/router";
import {TourServiceService} from "../../admin/components/tour/service/tour-service.service";
import {ProductService} from "../../admin/components/product/service/product.service";

@Component({
  selector: 'app-front',
  templateUrl: './front.component.html',
  styleUrls: ['./front.component.css']
})
export class FrontComponent {
  constructor(
              private router: Router,
              ) {
    // this.router.routeReuseStrategy.shouldReuseRoute = () => {
    //   return false;
    // };
  }


  async login() {
    await this.router.navigate(['/login']);
  }
  async register() {
    await this.router.navigate(['/register']);
  }
}
