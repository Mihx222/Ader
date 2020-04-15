import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {HomeComponent} from "./components/home/home.component";
import {LoginComponent} from "./components/login/login.component";
import {RegisterComponent} from "./components/register/register.component";
import {ProfileComponent} from "./components/profile/profile.component";
import {Role} from "./model/role/role.enum";
import {RoleGuard} from "./guards/role guard/role.guard";

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
    path: 'profile',
    component: ProfileComponent,
    canActivate: [RoleGuard],
    data: {
      requiredRoles: [Role.ROLE_USER, Role.ROLE_ADVERTISER]
    }
  }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
