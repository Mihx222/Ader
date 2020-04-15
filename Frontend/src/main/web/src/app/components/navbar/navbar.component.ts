import {Component, OnInit} from '@angular/core';
import {UserSharedDataService} from "../../service/user/user-shared-data.service";
import {UserViewModel} from "../../model/user/user-view-model";
import {AuthService} from "../../service/auth/auth.service";

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
        console.log(result);
      },
      error => {
        alert(error.error.error_description);
      }
    );
  }

  ngOnInit() {
  }
}
