import {Status} from "../status/status.enum";
import {OfferViewModel} from "../offer/offer-view-model";
import {BidViewModel} from "../bid/bid-view-model";

export interface UserViewModel {
  brandName: string;
  brandWebsite: string;
  email: string;
  createdOffers: OfferViewModel[];
  acceptedOffers: OfferViewModel[];
  bids: BidViewModel[];
  status: Status;
}
