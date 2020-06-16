import {UserViewModel} from "../user/user-view-model";

export interface File {
  uuid: string,
  user: UserViewModel,
  name: string,
  type: string,
  bytes: any
}
