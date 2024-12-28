import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {KathyComponent} from "../components/kathy/kathy.component";
import {KathyGuard} from "../../guard/kathy_guard/kathy.guard";
import {UserSettingsComponent} from "../components/user-settings/user-settings.component";
import {UserorderedKathyComponent} from "../components/user-settings/userordered-kathy/userordered-kathy.component";
import {UserorderKathyComponent} from "../components/user-settings/userorder-kathy/userorder-kathy.component";
import {CardComponent} from "../components/card/card.component";
import {CardOverviewComponent} from "../components/card-overview/card-overview.component";
import {SearchUserComponent} from "../components/search-user/search-user.component";

const routes: Routes = [
  {path: 'kathy', component: KathyComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_user_list', component: UserSettingsComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_users_ordered', component: UserorderedKathyComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_users_order', component: UserorderKathyComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_card', component: CardComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_card_overview', component: CardOverviewComponent,
    canActivate: [KathyGuard]},
  {path: 'kathy_search_user', component: SearchUserComponent,
    canActivate: [KathyGuard]},
];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [KathyGuard],
})
export class KathyRoutingModule { }
