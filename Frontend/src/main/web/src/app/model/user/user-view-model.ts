import {Status} from "../status/status.enum";
import {OfferViewModel} from "../offer/offer-view-model";
import {Role} from "../role/role.enum";

export interface UserViewModel {
  brandName: string;
  brandWebsite: string;
  email: string;
  roles: Role[];
  createdOffers: OfferViewModel[];
  status: Status;
}
