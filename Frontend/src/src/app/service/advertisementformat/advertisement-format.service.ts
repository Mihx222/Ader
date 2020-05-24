import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {AdvertisementFormat} from "../../model/advertisementformat/advertisement-format";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class AdvertisementFormatService {

  advertisementFormatURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getAdvertisementFormats() {
    return this.http.get<AdvertisementFormat[]>(this.advertisementFormatURL + 'advertisement-format?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  addAdvertisementFormat(advertisementFormat: AdvertisementFormat): Observable<AdvertisementFormat> {
    return this.http.post<AdvertisementFormat>(this.advertisementFormatURL + 'advertisement-format/add?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, advertisementFormat);
  }

  deleteAdvertisementFormat(advertisementFormatName: string): Observable<string> {
    return this.http.delete(this.advertisementFormatURL + 'advertisement-format/name/' + advertisementFormatName + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, {responseType: "text"});
  }
}
