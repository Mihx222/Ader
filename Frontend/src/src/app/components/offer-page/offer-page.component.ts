import {Component, OnInit} from '@angular/core';
import {Offer} from "../../model/offer/offer";
import {ActivatedRoute} from "@angular/router";

@Component({
  selector: 'app-offer-page',
  templateUrl: './offer-page.component.html',
  styleUrls: ['./offer-page.component.css']
})
export class OfferPageComponent implements OnInit {
  offer: Offer;
  offerImages: any[] = [];

  constructor(public activatedRoute: ActivatedRoute) {
    this.offer = this.activatedRoute.snapshot.data['offer'];
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
}
