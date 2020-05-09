import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Offer} from "../model/offer/offer";
import {Observable} from "rxjs";
import {OfferService} from "../service/offer/offer.service";
import {Injectable} from "@angular/core";

@Injectable({
  providedIn: "root"
})
export class OffersResolver implements Resolve<Offer[]> {

  constructor(private offerService: OfferService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Offer[]> | Promise<Offer[]> | Offer[] {
    return this.offerService.getOffers();
  }
}
