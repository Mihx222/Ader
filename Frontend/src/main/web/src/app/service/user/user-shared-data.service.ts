import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../../model/user/user';

@Injectable({
  providedIn: 'root'
})
export class UserSharedDataService {

  userData: BehaviorSubject<User>;
  userObservable: Observable<User>;

  constructor() {
    this.userData = new BehaviorSubject<User>(JSON.parse(localStorage.getItem("current_user")));
    this.userObservable = this.userData.asObservable();
  }

  setAuthenticatedUser(user: User) {
    this.userData.next(user);
  }

  getAuthenticatedUser(): Observable<User> {
    return this.userObservable;
  }
}
