import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';
import {UserSharedDataService} from "../user/user-shared-data.service";

@Injectable({
    providedIn: 'root'
})
export class ApiService {
    BASE_URL = 'http://localhost:8080/rest';
    TOKEN_URL = 'http://localhost:8080/oauth/token';
    CLIENT_NAME = 'networktap_api';
    CLIENT_SECRET = 'licenta2020';

    constructor(
        public http: HttpClient,
        public userSharedDataService: UserSharedDataService
    ) {
    }

    login(loginPayload: HttpParams) {
        const headers = {
            Authorization: 'Basic ' + btoa(this.CLIENT_NAME + ':' + this.CLIENT_SECRET),
            'Content-type': 'application/x-www-form-urlencoded'
        };
        return this.http.post<any>(this.TOKEN_URL, loginPayload.toString(), {headers});
  }

  logout() {
      localStorage.removeItem('current_user');
      localStorage.removeItem('token');
      this.userSharedDataService.userData.next(null);
  }
}
