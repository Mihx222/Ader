import {Status} from "../status/status.enum";
import {BidViewModel} from "../bid/bid-view-model";
import {CategoryViewModel} from "../category/category-view-model";
import {OfferMediaViewModel} from "../offermedia/offer-media-view-model";
import {OfferStatus} from "../offerstatus/offer-status.enum";
import DateTimeFormat = Intl.DateTimeFormat;

export interface Offer {
  id: number;
  name: string;
  description: string;
  authorName: string;
  assigneeName: string;
  bids: BidViewModel[];
  categories: CategoryViewModel[];
  offerMedia: OfferMediaViewModel[];
  offerStatus: OfferStatus;
  expireDate: DateTimeFormat;
  created: DateTimeFormat;
  updated: DateTimeFormat;
  status: Status;
}
