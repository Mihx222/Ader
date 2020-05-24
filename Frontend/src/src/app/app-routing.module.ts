import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {BrowseOffersComponent} from "./components/browse-offers/browse-offers.component";
import {BrowseInfluencersComponent} from "./components/browse-influencers/browse-influencers.component";
import {AuthGuard} from "./guards/auth guard/auth.guard";
import {NewOfferComponent} from "./components/new-offer/new-offer.component";
import {RoleGuard} from "./guards/role guard/role.guard";
import {Role} from "./model/role/role.enum";
import {OfferPageComponent} from "./components/offer-page/offer-page.component";
import {OfferResolver} from "./resolvers/offer-resolver.service";
import {OffersResolver} from "./resolvers/offers-resolver.service";
import {ProfileResolver} from "./resolvers/profile-resolver.service";
import {DashboardComponent} from "./components/admin/dashboard/dashboard.component";
import {CategoriesComponent} from "./components/admin/categories/categories.component";
import {CategoryResolver} from "./resolvers/category-resolver.service";
import {AdvertisementFormatsComponent} from "./components/admin/advertisement-formats/advertisement-formats.component";
import {AdvertisementFormatsResolver} from "./resolvers/advertisement-formats-resolver.service";

const routes: Routes = [
  {
    path: '',
    component: HomeComponent
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'profile/:email',
    component: ProfileComponent,
    resolve: {
      user: ProfileResolver
    }
  },
  {
    path: 'offers',
    component: BrowseOffersComponent,
    resolve: {
      offers: OffersResolver
    }
  },
  {
    path: 'influencers',
    component: BrowseInfluencersComponent
  },
  {
    path: 'offers/new',
    component: NewOfferComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      requiredRoles: [Role.ROLE_ADVERTISER]
    }
  },
  {
    path: 'offers/:id',
    component: OfferPageComponent,
    resolve: {
      offer: OfferResolver
    }
  },
  {
    path: 'admin',
    component: DashboardComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      requiredRoles: [Role.ROLE_ADMIN]
    }
  },
  {
    path: 'admin/categories',
    component: CategoriesComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      requiredRoles: [Role.ROLE_ADMIN]
    },
    resolve: {
      categories: CategoryResolver
    }
  },
  {
    path: 'admin/advertisement-formats',
    component: AdvertisementFormatsComponent,
    canActivate: [AuthGuard, RoleGuard],
    data: {
      requiredRoles: [Role.ROLE_ADMIN]
    },
    resolve: {
      formats: AdvertisementFormatsResolver
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
