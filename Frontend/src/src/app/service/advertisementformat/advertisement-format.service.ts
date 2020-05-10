import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {AdvertisementFormat} from "../../model/advertisementformat/advertisement-format";

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
}
