import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import decode from 'jwt-decode';
import {AuthService} from "../../service/auth/auth.service";
import {Role} from "../../model/role/role.enum";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
      public auth: AuthService,
      public router: Router) {
  }

  canActivate(
      route: ActivatedRouteSnapshot, state: RouterStateSnapshot):
      Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // this will be passed from the route config on the data property
    let requiredRoles: Role[] = route.data.requiredRoles;
    let containsRole: boolean = false;
    const rawToken = localStorage.getItem('token');
    let token: string;

    if (rawToken) {
      token = JSON.parse(localStorage.getItem('token')).access_token;
    } else {
      this.router.navigate(['login']);
      return false;
    }

    // decode the token to get its payload
    const tokenPayload = decode(token);

    // Check if user has required roles
    requiredRoles.forEach(requiredRole => {
      tokenPayload.authorities.forEach(actualRole => {
        if (requiredRole === actualRole) containsRole = true;
      });
    });

    if (!this.auth.isAuthenticated() || !containsRole) {
      this.router.navigate(['/']);
      return false;
    }

    return true;
  }
}
