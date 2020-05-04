import {Component, OnInit} from '@angular/core';
import {UserSharedDataService} from "../../service/user/user-shared-data.service";
import {UserViewModel} from "../../model/user/user-view-model";
import {AuthService} from "../../service/auth/auth.service";
import {Role} from "../../model/role/role.enum";
import {User} from "../../model/user/user";

@Component({
  selector: 'app-navbar',
  templateUrl: './navbar.component.html',
  styleUrls: ['./navbar.component.css']
})
export class NavbarComponent implements OnInit {
  authenticatedUser: UserViewModel;

  constructor(
      userSharedDataService: UserSharedDataService,
      public authService: AuthService
  ) {
    userSharedDataService.getAuthenticatedUser().subscribe(
        result => {
          this.authenticatedUser = result;
        },
        error => {
          alert(error.error.error_description);
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
