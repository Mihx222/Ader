import {Injectable} from '@angular/core';
import {ApiService} from '../api/api.service';
import {HttpClient} from '@angular/common/http';
import {User} from '../../model/user/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  userURL = this.apiService.BASE_URL + '/';

  constructor(private apiService: ApiService, private http: HttpClient) {
  }

  getUsers() {
    return this.http.get<User[]>(this.userURL + 'user?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  getUser(email: string) {
    return this.http.get<User>(this.userURL + 'user/' + email + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }
}
