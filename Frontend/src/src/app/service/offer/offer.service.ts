import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {HttpClient} from "@angular/common/http";
import {Offer} from "../../model/offer/offer";
import {Observable} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class OfferService {

  offerUrl = this.authService.BASE_URL + '/';
  token;

  constructor(private authService: AuthService, private http: HttpClient) {
    localStorage.getItem('token') === null ? this.token = '' :
        this.token = '?access_token=' + JSON.parse(localStorage.getItem('token')).access_token;
  }

  getOffer(id: number): Observable<Offer> {
    return this.http.get<Offer>(this.offerUrl + 'offer/' + id + this.token);
  }

  deassignFromOffer(assigneeName: string, offerId: string): Observable<any> {
    return this.http.post(this.offerUrl + 'offer/deassign?assigneeName=' + assigneeName +
    '&offerId=' + offerId + '&access_token=' + JSON.parse(localStorage.getItem('token')).access_token, null);
  }

  getOfferByUserEmail(
      userEmail: string,
      hasBids?: boolean,
      assigned?: boolean,
      completed?: boolean
  ): Observable<Offer[]> {
    return this.http.get<Offer[]>(
        this.offerUrl + 'offer/user/' + userEmail + '?' +
        (hasBids === false ? '' : ('hasBids=' + hasBids + '&')) +
        (assigned === false ? '' : ('assigned=' + assigned + '&')) +
        (completed === false ? '' : ('completed=' + completed + '&')) +
        'access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token
    );
  }

  getOffers() {
    return this.http.get<Offer[]>(this.offerUrl + 'offer' + this.token);
  }

  createOffer(offer: Offer) {
    return this.http.post<Offer>(this.offerUrl + 'offer/add?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, offer);
  }
}
