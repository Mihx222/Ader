import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Offer} from "../model/offer/offer";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {OfferService} from "../service/offer/offer.service";

@Injectable({
  providedIn: "root"
})
export class OfferResolver implements Resolve<Offer> {

  constructor(private offerService: OfferService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Offer> | Promise<Offer> | Offer {
    return this.offerService.getOffer(route.params.id);
  }
}
