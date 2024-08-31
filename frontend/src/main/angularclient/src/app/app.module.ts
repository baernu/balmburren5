import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import {UserService} from './components/user/service/user-service.service'
import {FormsModule} from "@angular/forms";
import { HomeComponent } from './components/home/home.component';
import { HeaderComponent } from './components/header/header.component';
import { FooterComponent } from './components/footer/footer.component';
import { RegisterComponent } from './components/register/register.component';
import { CookieService } from 'ngx-cookie-service';
import { LoginComponent } from './components/login/login.component';
import { LogoutComponent } from './components/logout/logout.component';
import { AdminDashboardComponent} from "./admin/components/admin-dashboard/admin-dashboard.component";
import {AdminModule} from "./admin/admin.module";
import {DatePipe, LocationStrategy, PathLocationStrategy} from "@angular/common";
import { OrderComponent } from './components/order/order.component';
import { UserorderprofileComponent } from './components/userorderprofile/userorderprofile.component';
import { InvoiceComponent } from './components/invoice/invoice.component';
import { SettingsComponent } from './components/settings/settings.component';
import {DriverModule} from "./driver/driver/driver.module";
import {KathyModule} from "./kathy/kathy/kathy.module";
import { WorkComponent } from './driver/components/work/work.component';
import { WagepaymentComponent } from './driver/components/wagepayment/wagepayment.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    RegisterComponent,
    LoginComponent,
    LogoutComponent,
    AdminDashboardComponent,
    OrderComponent,
    UserorderprofileComponent,
    InvoiceComponent,
    SettingsComponent,
    WorkComponent,
    WagepaymentComponent,
  ],
    imports: [
        BrowserModule,
        AppRoutingModule,
        FormsModule,
        HttpClientModule,
        AdminModule,
        DriverModule,
        KathyModule,
    ],
  providers: [UserService, CookieService,DatePipe, {provide: LocationStrategy, useClass: PathLocationStrategy} ],
  bootstrap: [AppComponent]
})

export class AppModule {

}
