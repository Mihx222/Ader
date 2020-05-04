import {Component, OnInit} from '@angular/core';
import {UserSharedDataService} from "../../service/user/user-shared-data.service";
import {UserViewModel} from "../../model/user/user-view-model";

@Component({
  selector: 'app-profile',
  templateUrl: './profile.component.html',
  styleUrls: ['./profile.component.css']
})
export class ProfileComponent implements OnInit {
  authenticatedUser: UserViewModel;

  constructor(userSharedDataService: UserSharedDataService) {
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
}
