import {Injectable} from '@angular/core';
import {AuthService} from "../auth/auth.service";
import {Observable} from "rxjs";
import {Bid} from "../../model/bid/bid";
import {HttpClient} from "@angular/common/http";
import {BidViewModel} from "../../model/bid/bid-view-model";

@Injectable({
  providedIn: 'root'
})
export class BidService {

  bidURL = this.authService.BASE_URL + '/';

  constructor(private authService: AuthService, private http: HttpClient) {
  }

  getBid(id: number): Observable<Bid> {
    return this.http.get<Bid>(this.bidURL + 'bid/' + id + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  acceptBids(bids: BidViewModel[]) {
    return this.http.post(this.bidURL + 'bid/accept' + '?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, bids);
  }

  getBidByUserEmailAndOfferId(userEmail: string, offerId: number): Observable<Bid> {
    return this.http.get<Bid>(
        this.bidURL + 'bid/get' + '?userEmail=' + userEmail + '&offerId=' + offerId + '&access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token);
  }

  placeBid(bid: Bid) {
    return this.http.post<Bid>(this.bidURL + 'bid/add?access_token=' +
        JSON.parse(localStorage.getItem('token')).access_token, bid);
  }
}
