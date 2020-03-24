import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {CustomErrorStateMatcher} from '../../helpers/custom-error-state-matcher';

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  matcher = new CustomErrorStateMatcher();
  usernameFormControl = new FormControl('', [Validators.required]);
  passwordFormControl = new FormControl('', [Validators.required]);
  firstNameFormControl = new FormControl('', [Validators.required]);
  lastNameFormControl = new FormControl('', [Validators.required]);
  emailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);

  constructor() {
  }

  ngOnInit() {
  }
}
