import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {User} from '../../model/user/user';
import {AuthService} from "../auth/auth.service";

@Injectable({
  providedIn: 'root'
})
export class UserService {

  userURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getUsers() {
    return this.http.get<User[]>(this.userURL + 'user?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  getUser(email: string) {
    let token;
    if (localStorage.getItem('token') === null) token = ''
    else token = '?access_token=' + JSON.parse(localStorage.getItem('token')).access_token;

    return this.http.get<User>(this.userURL + 'user/' + email + token);
  }
}
