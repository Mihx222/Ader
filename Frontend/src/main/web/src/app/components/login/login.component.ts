import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {CustomErrorStateMatcher} from '../../helpers/custom-error-state-matcher';
import {ActivatedRoute, Router} from '@angular/router';
import {ApiService} from '../../service/api/api.service';
import {HttpParams} from '@angular/common/http';
import {UserService} from '../../service/user/user.service';
import {UserSharedDataService} from '../../service/user/user-shared-data.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
  matcher = new CustomErrorStateMatcher();
  usernameFormControl = new FormControl('', [Validators.required]);
  passwordFormControl = new FormControl('', [Validators.required]);
  returnUrl: string;

  constructor(
    public router: Router,
    public route: ActivatedRoute,
    public apiService: ApiService,
    public userService: UserService,
    public userSharedDataService: UserSharedDataService
  ) {
  }

  ngOnInit() {
    // get return url from route parameters or default to '/'
    this.returnUrl = this.route.snapshot.queryParams.returnUrl || '/';
  }

  onSubmit() {
    // reset login status
    this.apiService.logout();

    const body = new HttpParams()
      .set('username', this.usernameFormControl.value)
      .set('password', this.passwordFormControl.value)
      .set('grant_type', 'password');

    this.apiService.login(body).subscribe(
      data => {
        localStorage.setItem('token', JSON.stringify(data));
        console.log(localStorage.getItem('token'));

        this.userService.getUser(this.usernameFormControl.value).subscribe(
          user => {
            this.userSharedDataService.setUser(user);
            user.token = JSON.parse(localStorage.getItem('token')).access_token;
            user.refresh_token = JSON.parse(localStorage.getItem('token')).refresh_token;

            localStorage.setItem('current_user', JSON.stringify(user));
            console.log(JSON.parse(localStorage.getItem('current_user')));
          },
          error => {
            alert('Login failed!');
          }
        );
        this.router.navigate(['/']);
      },
      error => {
        alert(error.error.error_description);
      });
  }
}
