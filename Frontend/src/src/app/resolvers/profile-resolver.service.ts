import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {UserViewModel} from "../model/user/user-view-model";
import {Observable} from "rxjs";
import {UserService} from "../service/user/user.service";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class ProfileResolver implements Resolve<UserViewModel> {

  constructor(private userService: UserService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserViewModel> | Promise<UserViewModel> | UserViewModel {
    return this.userService.getUser(route.params.email);
  }
}
