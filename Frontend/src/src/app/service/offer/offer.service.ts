import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {Offer} from "../../model/offer/offer";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OfferService {

  userURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getOffer(id: number): Observable<Offer> {
    return this.http.get<Offer>(this.userURL + 'offer/' + id + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  getOfferByUserEmail(
      userEmail: string,
      hasBids?: boolean,
      assigned?: boolean,
      completed?: boolean
  ): Observable<Offer[]> {
    return this.http.get<Offer[]>(
        this.userURL + 'offer/user/' + userEmail + '?' +
        (hasBids === false ? '' : ('hasBids=' + hasBids + '&')) +
        (assigned === false ? '' : ('assigned=' + assigned + '&')) +
        (completed === false ? '' : ('completed=' + completed + '&')) +
        'access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token
    );
  }

  getOffers() {
    return this.http.get<Offer[]>(this.userURL + 'offer?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  createOffer(offer: Offer) {
    return this.http.post<Offer>(this.userURL + 'offer/add?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, offer);
  }
}
