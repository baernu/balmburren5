import { Component } from '@angular/core';
import { Router } from '@angular/router';


@Component({
  selector: 'app-front',
  templateUrl: './front.component.html',
  styleUrls: ['./front.component.css']
})
export class FrontComponent {
  constructor(
    private router: Router,) {}


  async login() {
    await this.router.navigate(['/login']);
  }
  async register() {
    await this.router.navigate(['/register']);
  }
}
