import {UserViewModel} from "../user/user-view-model";
import {Status} from "../status/status.enum";
import DateTimeFormat = Intl.DateTimeFormat;

export interface OfferViewModel {
  description: string;
  author: UserViewModel;
  created: DateTimeFormat;
  updated: DateTimeFormat;
  status: Status;
}
