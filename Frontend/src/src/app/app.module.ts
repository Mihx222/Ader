import {BrowserModule} from '@angular/platform-browser';
import {NgModule} from '@angular/core';

import {AppRoutingModule} from './app-routing.module';
import {AppComponent} from './app.component';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {NavbarComponent} from "./components/navbar/navbar.component";
import {RegisterComponent} from "./components/register/register.component";
import {FormsModule, ReactiveFormsModule} from "@angular/forms";
import {BrowserAnimationsModule} from '@angular/platform-browser/animations';
import {MatToolbarModule} from "@angular/material/toolbar";
import {MatCardModule} from "@angular/material/card";
import {MatButtonModule} from "@angular/material/button";
import {MatNativeDateModule, MatRippleModule} from "@angular/material/core";
import {MatFormFieldModule} from "@angular/material/form-field";
import {MatInputModule} from "@angular/material/input";
import {MatSelectModule} from "@angular/material/select";
import {HttpClientModule} from "@angular/common/http";
import {FlexLayoutModule} from '@angular/flex-layout';
import {MatMenuModule} from "@angular/material/menu";
import {ProfileComponent} from './components/profile/profile.component';
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {BrowseOffersComponent} from './components/browse-offers/browse-offers.component';
import {BrowseInfluencersComponent} from './components/browse-influencers/browse-influencers.component';
import {NewOfferComponent} from './components/new-offer/new-offer.component';
import {MatTableModule} from "@angular/material/table";
import {MatPaginatorModule} from "@angular/material/paginator";
import {MatSortModule} from "@angular/material/sort";
import {ScrollUpButtonComponent} from './components/scroll-up-button/scroll-up-button.component';
import {MatChipsModule} from "@angular/material/chips";
import {MatIconModule} from "@angular/material/icon";
import {LoadingBarRouterModule} from "@ngx-loading-bar/router";
import {MaterialFileUploadComponent} from './components/materialfileupload/material-file-upload.component';
import {MatProgressBarModule} from "@angular/material/progress-bar";
import {MatDatepickerModule} from "@angular/material/datepicker";
import {OfferPageComponent} from './components/offer-page/offer-page.component';
import {MatTabsModule} from "@angular/material/tabs";
import {GalleriaModule} from "primeng";
import {MatSlideToggleModule} from "@angular/material/slide-toggle";
import {MatDividerModule} from "@angular/material/divider";
import {MatCheckboxModule} from "@angular/material/checkbox";
import {DashboardComponent} from './components/admin/dashboard/dashboard.component';
import {MatSidenavModule} from "@angular/material/sidenav";
import {MatListModule} from "@angular/material/list";
import {CategoriesComponent} from './components/admin/categories/categories.component';
import { AdvertisementFormatsComponent } from './components/admin/advertisement-formats/advertisement-formats.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    NavbarComponent,
    RegisterComponent,
    ProfileComponent,
    BrowseOffersComponent,
    BrowseInfluencersComponent,
    NewOfferComponent,
    ScrollUpButtonComponent,
    MaterialFileUploadComponent,
    OfferPageComponent,
    DashboardComponent,
    CategoriesComponent,
    AdvertisementFormatsComponent
  ],
  imports: [
    BrowserModule,
    FlexLayoutModule,
    AppRoutingModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatRippleModule,
    MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatInputModule,
    FormsModule,
    ReactiveFormsModule,
    MatSelectModule,
    HttpClientModule,
    MatMenuModule,
    MatTableModule,
    MatPaginatorModule,
    MatSortModule,
    MatChipsModule,
    MatIconModule,
    LoadingBarRouterModule,
    MatProgressBarModule,
    MatDatepickerModule,
    MatNativeDateModule,
    MatTabsModule,
    GalleriaModule,
    MatSlideToggleModule,
    MatDividerModule,
    MatCheckboxModule,
    MatSidenavModule,
    MatListModule
  ],
  providers: [
    {provide: JWT_OPTIONS, useValue: JWT_OPTIONS},
    JwtHelperService
  ],
  bootstrap: [AppComponent]
})
export class AppModule {
}
