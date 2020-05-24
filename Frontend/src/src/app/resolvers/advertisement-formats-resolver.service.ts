import {ActivatedRouteSnapshot, Resolve, RouterStateSnapshot} from "@angular/router";
import {Injectable} from "@angular/core";
import {Observable} from "rxjs";
import {AdvertisementFormat} from "../model/advertisementformat/advertisement-format";
import {AdvertisementFormatService} from "../service/advertisementformat/advertisement-format.service";

@Injectable({
  providedIn: "root"
})
export class AdvertisementFormatsResolver implements Resolve<AdvertisementFormat[]> {

  constructor(private advertisementFormatService: AdvertisementFormatService) {
  }

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<AdvertisementFormat[]> | Promise<AdvertisementFormat[]> | AdvertisementFormat[] {
    return this.advertisementFormatService.getAdvertisementFormats();
  }
}
