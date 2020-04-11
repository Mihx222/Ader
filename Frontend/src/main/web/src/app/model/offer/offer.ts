import {UserViewModel} from "../user/user-view-model";
import {Status} from "../status/status.enum";
import DateTimeFormat = Intl.DateTimeFormat;

export interface Offer {
  id: number;
  description: string;
  author: UserViewModel;
  created: DateTimeFormat;
  updated: DateTimeFormat;
  status: Status;
}
