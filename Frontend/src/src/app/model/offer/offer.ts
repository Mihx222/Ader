import {Status} from "../status/status.enum";
import {BidViewModel} from "../bid/bid-view-model";
import {CategoryViewModel} from "../category/category-view-model";
import {FileViewModel} from "../offermedia/file-view-model";
import {OfferStatus} from "../offerstatus/offer-status.enum";
import {AdvertisementFormatViewModel} from "../advertisementformat/advertisement-format-view-model";
import DateTimeFormat = Intl.DateTimeFormat;

export interface Offer {
  id: number;
  name: string;
  description: string;
  authorName?: string;
  assigneeName: string;
  bids: BidViewModel[];
  categories: CategoryViewModel[];
  files: FileViewModel[];
  expireDate: Date;
  advertisementFormats: AdvertisementFormatViewModel[];
  freeProductSample: boolean;
  advertisementReview: boolean;
  compensation: string;
  offerStatus?: OfferStatus;
  created?: DateTimeFormat;
  updated?: DateTimeFormat;
  status?: Status;
}
