import {Status} from "../status/status.enum";
import {OfferViewModel} from "../offer/offer-view-model";
import {Role} from "../role/role.enum";
import {BidViewModel} from "../bid/bid-view-model";
import {PersonaViewModel} from "../persona/persona-view-model";

export interface User {
  id: number;
  brandName: string;
  brandWebsite: string;
  email: string;
  password?: string;
  roles: Role[];
  createdOffers: OfferViewModel[];
  acceptedOffers: OfferViewModel[];
  bids: BidViewModel[];
  personas: PersonaViewModel[];
  status: Status;
  token?: string;
  refresh_token?: string;
  token_expiration?: number;
}
