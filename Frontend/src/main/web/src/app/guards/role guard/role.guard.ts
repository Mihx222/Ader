import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import decode from 'jwt-decode';
import {AuthService} from "../../service/auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class RoleGuard implements CanActivate {

  constructor(
    public auth: AuthService,
    public router: Router) {
  }

  canActivate(
    route: ActivatedRouteSnapshot, state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // this will be passed from the route config on the data property
    const expectedRole = route.data.expectedRole;
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

    if (!this.auth.isAuthenticated() || tokenPayload.role !== expectedRole) {
      this.router.navigate(['login']);
      return false;
    }

    return true;
  }
}
