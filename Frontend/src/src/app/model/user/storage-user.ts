import {Role} from "../role/role.enum";
import {Status} from "../status/status.enum";

export interface StorageUser {
  id: number;
  brandName: string;
  brandWebsite: string;
  email: string;
  roles: Role[];
  status: Status;
  token?: string;
  refresh_token?: string;
  token_expiration?: number;
}
