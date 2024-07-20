import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import {UserListComponent} from './components/user/user-list/user-list.component';
import { AdminRoutingModule } from './admin-routing.module';
import { HeaderComponent } from './components/header/header.component';
import { HomeComponent } from './components/home/home.component';
import { TourComponent } from "./components/tour/create_tour/tour.component";
import {FormsModule} from "@angular/forms";
import { TourDataComponent } from './components/tour/tour dates/tour_data/tour-data.component';
import { AddDatesComponent } from './components/tour/tour dates/add_data/add-dates/add-dates.component';
import { AddRoleComponent } from './components/user/add-role/add-role.component';
import { UserbindtourComponent } from './components/userbind/userbindtour/userbindtour.component';
import { ProductComponent } from './components/product/product.component';
import { CreateComponent } from './components/product/create_product/create.component';
import { CreateProductDetailsComponent } from './components/product/create-product-details/create-product-details.component';
import { UserTourComponent } from './components/tour/user-tour/user-tour.component';
import { InvoiceAdminComponent } from './components/invoice-admin/invoice-admin.component';
import {UserFormComponent} from "./components/user/user-form/user-form.component";
import { SettingsComponent } from './components/user/settings/settings.component';
import { UserOrderComponent } from './components/user/user-order/user-order.component';
import { UserOrderedComponent } from './components/user/user-ordered/user-ordered.component';
import { EmailComponent } from './components/email/email.component';
import { InvoiceEmailComponent } from './components/invoice-admin/invoice-email/invoice-email.component';
import { InvoiceEmailPreviewComponent } from './components/invoice-admin/invoice-email-preview/invoice-email-preview.component';


@NgModule({
  declarations: [
    HeaderComponent,
    HomeComponent,
    TourComponent,
    TourDataComponent,
    AddDatesComponent,
    AddRoleComponent,
    UserbindtourComponent,
    ProductComponent,
    CreateComponent,
    CreateProductDetailsComponent,
    UserTourComponent,
    InvoiceAdminComponent,
    UserListComponent,
    UserFormComponent,
    SettingsComponent,
    UserOrderComponent,
    UserOrderedComponent,
    EmailComponent,
    InvoiceEmailComponent,
    InvoiceEmailPreviewComponent,
  ],
  exports: [
    HeaderComponent
  ],
    imports: [
        CommonModule,
        AdminRoutingModule,
        FormsModule,
    ]
})
export class AdminModule { }
