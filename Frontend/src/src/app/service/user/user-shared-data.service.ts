import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {StorageUser} from "../../model/user/storage-user";

@Injectable({
  providedIn: 'root'
})
export class UserSharedDataService {

  userData: BehaviorSubject<StorageUser>;
  userObservable: Observable<StorageUser>;

  constructor() {
    this.userData = new BehaviorSubject<StorageUser>(JSON.parse(localStorage.getItem("current_user")));
    this.userObservable = this.userData.asObservable();
  }

  setAuthenticatedUser(user: StorageUser) {
    this.userData.next(user);
  }

  getAuthenticatedUser(): Observable<StorageUser> {
    return this.userObservable;
  }
}
