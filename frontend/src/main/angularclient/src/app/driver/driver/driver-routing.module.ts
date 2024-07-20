import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {DriverGuard} from "../../guard/driver_guard/driver.guard";
import {DriverComponent} from "../components/driver/driver.component";
import {ActualTourComponent} from "../components/actual-tour/actual-tour.component";

const routes: Routes = [
  {path: 'driver', component: DriverComponent,
    canActivate: [DriverGuard]},
  {path: 'driver_actual_tour', component: ActualTourComponent,
    canActivate: [DriverGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [DriverGuard],
})
export class DriverRoutingModule { }
