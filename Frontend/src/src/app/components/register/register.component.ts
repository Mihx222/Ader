import {Component, OnInit} from '@angular/core';
import {FormControl, Validators} from '@angular/forms';
import {CustomErrorStateMatcher} from '../../helpers/custom-error-state-matcher';
import {AccountType} from "../../model/account-type.enum";
import {User} from "../../model/user/user";
import {AuthService} from "../../service/auth/auth.service";
import {Role} from "../../model/role/role.enum";
import {Router} from "@angular/router";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  matcher = new CustomErrorStateMatcher();
  influencerEmailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  advertiserEmailFormControl = new FormControl('', [
    Validators.required,
    Validators.email,
  ]);
  influencerPasswordFormControl = new FormControl('', [Validators.required]);
  influencerConfirmPasswordFormControl = new FormControl('', [Validators.required]);
  advertiserPasswordFormControl = new FormControl('', [Validators.required]);
  advertiserConfirmPasswordFormControl = new FormControl('', [Validators.required]);
  brandNameFormControl = new FormControl('', [Validators.required]);
  brandWebsiteFormControl = new FormControl('');
  accountType: string;
  selected = new FormControl(0);

  constructor(public authService: AuthService, public router: Router) {
  }

  ngOnInit() {
  }

  nextTab() {
    let currentValue: number = this.selected.value;
    this.selected.setValue(++currentValue);
  }

  asInfluencer(): boolean {
    return this.accountType === AccountType.INFLUENCER
  }

  asAdvertiser(): boolean {
    return this.accountType === AccountType.ADVERTISER
  }

  register() {
    let newUser: User = {
      acceptedOffers: [],
      bids: [],
      brandName: this.brandNameFormControl.value === '' ? null : this.brandNameFormControl.value,
      brandWebsite: this.brandWebsiteFormControl.value === '' ? null : this.brandWebsiteFormControl.value,
      createdOffers: [],
      email: this.accountType === AccountType.INFLUENCER ? this.influencerEmailFormControl.value : this.advertiserEmailFormControl.value,
      id: null,
      password: this.accountType === AccountType.INFLUENCER ? this.influencerPasswordFormControl.value : this.advertiserPasswordFormControl.value,
      personas: [],
      roles: [],
      status: undefined
    };

    let newUserRole: string = this.accountType === AccountType.ADVERTISER ? Role[Role.ROLE_ADVERTISER] : Role[Role.ROLE_USER]
    this.authService.register(newUser, newUserRole).subscribe(
        result => {
          this.router.navigate(['/login']);
        },
        error => {
          console.log(error);
        }
    );
  }
}
