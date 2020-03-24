import {RoleViewModel} from '../role/role-view-model';

export interface User {
  id: number;
  username: string;
  password?: string;
  firstName: string;
  lastName: string;
  email: string;
  roles: RoleViewModel[];
  token?: string;
  refresh_token?: string;
}
