import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { KathyRoutingModule } from './kathy-routing.module';
import {HeaderComponent} from "../components/header/header.component";
import {FormsModule} from "@angular/forms";
import {UserSettingsComponent} from "../components/user-settings/user-settings.component"
import {KathyComponent} from "../components/kathy/kathy.component";
import {UserorderedKathyComponent} from "../components/user-settings/userordered-kathy/userordered-kathy.component";
import {UserorderKathyComponent} from "../components/user-settings/userorder-kathy/userorder-kathy.component";
import {CardComponent} from "../components/card/card.component";

@NgModule({
  declarations: [
    HeaderComponent,
    UserSettingsComponent,
    KathyComponent,
    UserorderedKathyComponent,
    UserorderKathyComponent,
    CardComponent
  ],
  exports: [
    HeaderComponent
  ],
  imports: [
    CommonModule,
    KathyRoutingModule,
    FormsModule
  ]
})
export class KathyModule { }
