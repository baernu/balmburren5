import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {RegisterComponent} from "./components/register/register.component";
import {LoginComponent} from "./components/login/login.component";
import {LogoutComponent} from "./components/logout/logout.component";
import {AuthGuardGuard} from "./guard/auth.guard.guard";
import {AdminDashboardComponent} from "./admin/components/admin-dashboard/admin-dashboard.component";
import {OrderComponent} from "./components/order/order.component";
import {UserorderprofileComponent} from "./components/userorderprofile/userorderprofile.component";
import {InvoiceComponent} from "./components/invoice/invoice.component";
import {SettingsComponent} from "./components/settings/settings.component";
import {BasicGuard} from "./guard/basic_guard/basic.guard";
import {DriverComponent} from "./driver/components/driver/driver.component";
import {DriverGuard} from "./guard/driver_guard/driver.guard";
import {WorkComponent} from "./driver/components/work/work.component";
import {WagepaymentComponent} from "./driver/components/wagepayment/wagepayment.component";

const routes: Routes = [
  { path: '', component: HomeComponent},
  { path: 'home', component: HomeComponent},
  { path: 'register', component: RegisterComponent},
  { path: 'login', component: LoginComponent},
  { path: 'logout', component: LogoutComponent},
  {path: 'admin', component: AdminDashboardComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'basic_order', component: OrderComponent,
    canActivate: [BasicGuard]},
  {path: 'basic_order_profil', component: UserorderprofileComponent,
    canActivate: [BasicGuard]},
  {path: 'basic_invoice', component: InvoiceComponent,
    canActivate: [BasicGuard]},
  {path: 'settings', component: SettingsComponent},
  {path: 'driver', component: DriverComponent,
    canActivate: [DriverGuard]},
  {path: 'work', component: WorkComponent,
    canActivate: [DriverGuard]},
  {path: 'wage_payment', component: WagepaymentComponent,
    canActivate: [DriverGuard]},
];

@NgModule({
  imports: [RouterModule.forRoot(routes, { onSameUrlNavigation: 'reload' })],
  exports: [RouterModule]
})
export class AppRoutingModule { }
