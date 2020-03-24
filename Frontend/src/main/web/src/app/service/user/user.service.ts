import {Injectable} from '@angular/core';
import {ApiService} from '../api/api.service';
import {HttpClient} from '@angular/common/http';
import {User} from '../../model/user/user';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  userURL = this.apiService.baseURL + '/';

  constructor(private apiService: ApiService, private http: HttpClient) {
  }

  getUsers() {
    return this.http.get<User[]>(this.userURL + 'user?access_token=' +
      JSON.parse(localStorage.getItem('token')).access_token);
  }

  getUser(username: string) {
    return this.http.get<User>(this.userURL + 'user/' + username + '?access_token=' +
      JSON.parse(localStorage.getItem('token')).access_token);
  }
}
