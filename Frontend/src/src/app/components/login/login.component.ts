import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {CustomErrorStateMatcher} from '../../helpers/custom-error-state-matcher';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpParams} from '@angular/common/http';
import {UserService} from '../../service/user/user.service';
import {UserSharedDataService} from '../../service/user/user-shared-data.service';
import {AuthService} from "../../service/auth/auth.service";
import {StorageUser} from "../../model/user/storage-user";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  matcher = new CustomErrorStateMatcher();
  emailFormControl = new FormControl('', [Validators.required, Validators.email]);
  passwordFormControl = new FormControl('', [Validators.required]);
  returnUrl: string;

  constructor(
      public router: Router,
      public route: ActivatedRoute,
      public authService: AuthService,
      public userService: UserService,
      public userSharedDataService: UserSharedDataService
  ) {
  }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmit() {
    if (this.emailFormControl.hasError('required') ||
        this.emailFormControl.hasError('email') ||
        this.passwordFormControl.hasError('required')) {
      return;
    }

    // reset login status
    this.authService.logout();

    const body = new HttpParams()
        .set('username', this.emailFormControl.value)
        .set('password', this.passwordFormControl.value)
        .set('grant_type', 'password');

    this.authService.login(body).subscribe(
        data => {
          if (localStorage.getItem('token') !== null) {
            localStorage.removeItem('token');
          }
          localStorage.setItem('token', JSON.stringify(data));

          this.userService.getUser(this.emailFormControl.value).subscribe(
              user => {
                let storageUser: StorageUser = {
                  id: user.id,
                  brandName: user.brandName,
                  brandWebsite: user.brandWebsite,
                  email: user.email,
                  roles: user.roles,
                  status: user.status,
                  token: JSON.parse(localStorage.getItem('token')).access_token,
                  refresh_token: JSON.parse(localStorage.getItem('token')).refresh_token,
                  token_expiration: JSON.parse(localStorage.getItem('token')).expires_in
                };

                this.userSharedDataService.setAuthenticatedUser(storageUser);

                if (localStorage.getItem('current_user') !== null) {
                  localStorage.removeItem('current_user');
                }
                localStorage.setItem('current_user', JSON.stringify(storageUser));
              },
              error => {
                alert(error.error.error_description);
              }
          );
          this.router.navigate(['/']);
        },
        error => {
          alert(error.error.error_description);
        });
  }
}
