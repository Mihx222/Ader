import {UserViewModel} from "../user/user-view-model";
import {Status} from "../status/status.enum";
import {BidViewModel} from "../bid/bid-view-model";
import {CategoryViewModel} from "../category/category-view-model";
import {OfferMediaViewModel} from "../offermedia/offer-media-view-model";
import {OfferStatus} from "../offerstatus/offer-status.enum";
import DateTimeFormat = Intl.DateTimeFormat;

export interface OfferViewModel {
  name: string;
  description: string;
  author: UserViewModel;
  assignee: UserViewModel;
  bids: BidViewModel[];
  categories: CategoryViewModel[];
  offerMedia: OfferMediaViewModel[];
  offerStatus: OfferStatus;
  expireDate: DateTimeFormat;
  created: DateTimeFormat;
  updated: DateTimeFormat;
  status: Status;
}
