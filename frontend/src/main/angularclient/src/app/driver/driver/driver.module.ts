import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { DriverRoutingModule } from './driver-routing.module';
import {HeaderComponent} from "../components/header/header.component";
import {FormsModule} from "@angular/forms";
import {ActualTourComponent} from "../components/actual-tour/actual-tour.component";
import {DriverComponent} from "../components/driver/driver.component";
import {AdminModule} from "../../admin/admin.module";


@NgModule({
  declarations: [
    HeaderComponent,
    ActualTourComponent,
    DriverComponent,
  ],
  exports: [
    HeaderComponent
  ],
    imports: [
        CommonModule,
        DriverRoutingModule,
        FormsModule,
        AdminModule,
    ]
})
export class DriverModule { }
