import {Component, OnInit} from '@angular/core';
import {UserSharedDataService} from "../../service/user/user-shared-data.service";
import {AuthService} from "../../service/auth/auth.service";
import {Role} from "../../model/role/role.enum";
import {User} from "../../model/user/user";
import {StorageUser} from "../../model/user/storage-user";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  authenticatedUser: StorageUser;

  constructor(
      userSharedDataService: UserSharedDataService,
      public authService: AuthService
  ) {
    userSharedDataService.getAuthenticatedUser().subscribe(
        result => {
          this.authenticatedUser = result;
        },
        error => {
          console.log(error);
        }
    );
  }

  ngOnInit() {
  }

  isAdvertiser(): boolean {
    let result: boolean = false;
    let current_user: User = JSON.parse(localStorage.getItem("current_user"));

    if (current_user !== null) {
      current_user.roles.forEach(role => {
        // @ts-ignore
        if (role.name === Role[Role.ROLE_ADVERTISER]) result = true;
      });
    }
    return result;
  }
}
