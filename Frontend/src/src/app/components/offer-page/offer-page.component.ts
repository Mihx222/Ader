import {Component, OnInit} from '@angular/core';
import {ActivatedRoute} from "@angular/router";
import {CustomErrorStateMatcher} from "../../helpers/custom-error-state-matcher";
import {Offer} from "../../model/offer/offer";
import {FormControl} from "@angular/forms";

@Component({
  selector: 'app-offer-page',
  templateUrl: './offer-page.component.html',
  styleUrls: ['./offer-page.component.css']
})
export class OfferPageComponent implements OnInit {
  matcher = new CustomErrorStateMatcher();
  whoIsYourAudience = new FormControl();
  whatDoYouDo = new FormControl();
  whatAreYouSelling = new FormControl();
  coreValues = new FormControl();
  personalityOfYourBrand = new FormControl();
  compensation = new FormControl({value: '', disabled: false});
  requirementsAccepted: boolean = false;
  offer: Offer;
  offerImages: any[] = [];
  offerExpirationDate;
  currentDate;
  timeToDisplay;
  offerExpired: boolean = false;

  constructor(public activatedRoute: ActivatedRoute) {
    this.offer = this.activatedRoute.snapshot.data['offer'];

    this.calculateDate();
  }

  ngOnInit() {
    this.offer.files.forEach(file => {
      if (file.type.includes('image')) {
        this.offerImages.push({
          source: 'data:' + file.type + ';base64,' + file.bytes,
          title: file.name
        });
      }
    })
  }

  calculateDate() {
    this.currentDate = new Date(Date.now());
    this.offerExpirationDate = new Date(this.offer.expireDate);

    this.timeToDisplay = Math.round(Math.abs(this.offerExpirationDate.getTime() - this.currentDate.getTime()) / 36e5);

    if (this.currentDate > this.offerExpirationDate) {
      this.timeToDisplay = -this.timeToDisplay;
      this.offerExpired = true;
    }
  }
}
