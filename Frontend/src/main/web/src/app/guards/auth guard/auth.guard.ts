import {Injectable} from '@angular/core';
import {ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree} from '@angular/router';
import {Observable} from 'rxjs';
import {UserSharedDataService} from "../../service/user/user-shared-data.service";
import {User} from "../../model/user/user";
import {Role} from "../../model/role/role.enum";

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {
  private authenticatedUser: User;

  constructor(
    public router: Router,
    public userSharedDataService: UserSharedDataService
  ) {
    userSharedDataService.getAuthenticatedUser().subscribe(
      result => {
        this.authenticatedUser = result;
      },
      error => {
        console.log(error.error.error_description);
      }
    );
  }

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    if (this.authenticatedUser) {
      let requiredRoles = route.data.requiredRoles;

      // check if route is restricted by role
      if (Object.values(this.authenticatedUser.roles).includes(Role.ROLE_ADMIN)) {
        return true;
      }

      if (requiredRoles &&
        Object.values(this.authenticatedUser.roles).some(r => requiredRoles.includes(r))
      ) {
        console.log("You do not have the required roles to access this page!");
        console.log("Required roles:");
        console.log(requiredRoles);
        console.log("Your roles:");
        console.log(this.authenticatedUser.roles);
        return true;
      }

      if (this.authenticatedUser.token_expiration === 0) {
        console.log("Token has expired!");
        return false;
      }

      this.router.navigate(['/']);
      return false;
    }

    // not logged in so redirect to login page with the return url
    this.router.navigate(['/login'], {queryParams: {returnUrl: state.url}});
    return false;
  }
}
