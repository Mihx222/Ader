import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Observable} from "rxjs";
import {Injectable} from "@angular/core";
import {UserViewModel} from "../model/user/user-view-model";
import {UserService} from "../service/user/user.service";

@Injectable({
  providedIn: "root"
})
export class UsersResolverService implements Resolve<UserViewModel[]> {

  constructor(private userService: UserService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<UserViewModel[]> | Promise<UserViewModel[]> | UserViewModel[] {
    return this.userService.getUsers();
  }
}
