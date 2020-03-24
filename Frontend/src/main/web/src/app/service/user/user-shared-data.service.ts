import {Injectable} from '@angular/core';
import {BehaviorSubject, Observable} from 'rxjs';
import {User} from '../../model/user/user';

@Injectable({
  providedIn: 'root'
})
export class UserSharedDataService {

  userData: BehaviorSubject<User> = new BehaviorSubject<User>(null);
  userObservable: Observable<User> = this.userData.asObservable();

  constructor() {
  }

  setUser(user: User) {
    this.userData.next(user);
  }
}
