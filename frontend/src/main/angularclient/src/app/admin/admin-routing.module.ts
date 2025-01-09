import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import {AdminDashboardComponent} from "./components/admin-dashboard/admin-dashboard.component";
import {HomeComponent} from "./components/home/home.component";
import {AuthGuardGuard} from "../guard/auth.guard.guard";
import {TourComponent} from "./components/tour/create_tour/tour.component";
import {TourDataComponent} from "./components/tour/tour dates/tour_data/tour-data.component";
import {AddDatesComponent} from "./components/tour/tour dates/add_data/add-dates/add-dates.component";
import {AddRoleComponent} from "./components/user/add-role/add-role.component";
import {UserbindtourComponent} from "./components/userbind/userbindtour/userbindtour.component";
import {ProductComponent} from "./components/product/product.component";
import {CreateComponent} from "./components/product/create_product/create.component";
import {CreateProductDetailsComponent} from "./components/product/create-product-details/create-product-details.component";
import {UserTourComponent} from "./components/tour/user-tour/user-tour.component";
import {InvoiceAdminComponent} from "./components/invoice-admin/invoice-admin.component";
import {UserListComponent} from "./components/user/user-list/user-list.component";
import {UserFormComponent} from "./components/user/user-form/user-form.component";
import {SettingsComponent} from "./components/user/settings/settings.component";
import {UserOrderComponent} from "./components/user/user-order/user-order.component";
import {UserOrderedComponent} from "./components/user/user-ordered/user-ordered.component";
import {EmailComponent} from "./components/email/email.component";
import {InvoiceEmailComponent} from "./components/invoice-admin/invoice-email/invoice-email.component";
import {
  InvoiceEmailPreviewComponent
} from "./components/invoice-admin/invoice-email-preview/invoice-email-preview.component";
import {DriverWorkComponent} from "./components/driver/driver-work/driver-work.component";
import {DriverOverviewComponent} from "./components/driver/driver-overview/driver-overview.component";
import {DriverComponent} from "./components/driver/driver.component";
import {SearchUserAdminComponent} from "./components/user/search-user-admin/search-user-admin.component";
// import { BackupComponent } from './components/backup/backup.component';

const routes: Routes = [
  {path: 'admin', component: AdminDashboardComponent,
        canActivate: [AuthGuardGuard]},
  {path: 'admin_home', component: HomeComponent,
        canActivate: [AuthGuardGuard]},
  {path: 'admin_tour', component: TourComponent,
        canActivate: [AuthGuardGuard]},
  {path: '', redirectTo: '/home', pathMatch: 'full'},
  {path: 'admin_tour_data', component: TourDataComponent,
        canActivate: [AuthGuardGuard]},
  {path: 'admin_tour_data_add',component: AddDatesComponent,
        canActivate: [AuthGuardGuard]},
  {path: 'admin_users_role', component: AddRoleComponent,
        canActivate: [AuthGuardGuard]},
  {path: 'admin_user_bind_tour', component: UserbindtourComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_product', component: ProductComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_product_create', component: CreateComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_product_details_create', component: CreateProductDetailsComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_tour_user', component: UserTourComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_invoice', component: InvoiceAdminComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_users', component: UserListComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_users_add', component: UserFormComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_users_settings', component: SettingsComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_users_order', component: UserOrderComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_users_ordered', component: UserOrderedComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_email', component: EmailComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_invoice_email', component: InvoiceEmailComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'admin_invoice_email_preview', component: InvoiceEmailPreviewComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'driver_work', component: DriverWorkComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'driver_overview', component: DriverOverviewComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'drivers', component: DriverComponent,
    canActivate: [AuthGuardGuard]},
  {path: 'search_user_admin', component: SearchUserAdminComponent,
    canActivate: [AuthGuardGuard]},

];

@NgModule({
  imports: [RouterModule.forChild(routes)],
  exports: [RouterModule],
  providers: [AuthGuardGuard],
})
export class AdminRoutingModule { }
