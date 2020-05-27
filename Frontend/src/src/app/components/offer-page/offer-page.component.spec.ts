import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {OfferPageComponent} from './offer-page.component';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";
import {MatTableModule} from "@angular/material/table";
import {ActivatedRoute} from "@angular/router";
import {Offer} from "../../model/offer/offer";
import {OfferStatus} from "../../model/offerstatus/offer-status.enum";

describe('OfferPageComponent', () => {
  let component: OfferPageComponent;
  let fixture: ComponentFixture<OfferPageComponent>;

  beforeEach(async(() => {
    let mockData: Offer = {
      advertisementFormats: [],
      advertisementReview: false,
      assigneeName: "mock",
      bids: [],
      categories: [],
      compensation: "mock",
      description: "mock",
      expireDate: undefined,
      files: [],
      freeProductSample: false,
      id: 0,
      name: "mock",
      offerStatus: OfferStatus.OPEN
    };

    TestBed.configureTestingModule({
      declarations: [OfferPageComponent],
      providers: [
        HttpClient,
        HttpHandler,
        JwtHelperService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get(): string {
                  return 'mock';
                },
              },
              data: {offer: mockData},
            },
          },
        },
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ],
      imports: [MatTableModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(OfferPageComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
