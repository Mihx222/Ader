import {Injectable} from '@angular/core';
import {HttpClient, HttpParams} from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  baseURL = 'http://localhost:8080/rest';
  tokenURL = 'http://localhost:8080/oauth/token';
  clientName = 'networktap_api';
  clientSecret = 'licenta2020';

  constructor(
    public http: HttpClient
  ) {
  }

  login(loginPayload: HttpParams) {
    const headers = {
      Authorization: 'Basic ' + btoa(this.clientName + ':' + this.clientSecret),
      'Content-type': 'application/x-www-form-urlencoded'
    };
    return this.http.post<any>(this.tokenURL, loginPayload.toString(), {headers});
  }

  logout() {
    localStorage.removeItem('current_user');
  }
}
