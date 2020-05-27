import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';

import {NewOfferComponent} from './new-offer.component';
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, DebugElement, NO_ERRORS_SCHEMA} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";
import {By} from "@angular/platform-browser";
import {Offer} from "../../model/offer/offer";
import {Observable} from "rxjs";

describe('NewOfferComponent', () => {
  let component: NewOfferComponent;
  let fixture: ComponentFixture<NewOfferComponent>;
  let debugElement: DebugElement;
  let httpClientSpy: { post: jasmine.Spy };

  beforeAll(() => {
    let store = {};
    const mockLocalStorage = {
      getItem: (key: string): string => {
        return key in store ? store[key] : null;
      },
      setItem: (key: string, value: string) => {
        store[key] = `${value}`;
      },
      removeItem: (key: string) => {
        delete store[key];
      },
      clear: () => {
        store = {};
      }
    };

    spyOn(localStorage, 'getItem')
        .and.callFake(mockLocalStorage.getItem);
    spyOn(localStorage, 'setItem')
        .and.callFake(mockLocalStorage.setItem);
    spyOn(localStorage, 'removeItem')
        .and.callFake(mockLocalStorage.removeItem);
    spyOn(localStorage, 'clear')
        .and.callFake(mockLocalStorage.clear);

    localStorage.setItem('token', JSON.stringify({
      access_token: 'mock',
      expires_in: 999,
      jti: 'mock',
      refresh_token: 'mock',
      scope: "mock mock mock",
      token_type: "mock"
    }));

    localStorage.setItem('current_user', JSON.stringify({
      brandName: "test",
      brandWebsite: "test",
      email: "test@test.com",
      id: 1,
      refresh_token: "mock",
      roles: [],
      token: "mock",
      token_expiration: 999
    }));
  });

  beforeEach(async(() => {
    httpClientSpy = jasmine.createSpyObj('HttpClient', ['post']);

    TestBed.configureTestingModule({
      declarations: [NewOfferComponent],
      providers: [
        JwtHelperService,
        {
          provide: Router, useClass: class {
            navigate = jasmine.createSpy("navigate");
          }
        },
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ],
      imports: [HttpClientTestingModule, RouterTestingModule],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(NewOfferComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('Create button should be disabled when required fields are not set', async(() => {
    debugElement = fixture.debugElement.query(By.css('#createButton'));
    expect(debugElement.nativeElement.disabled).toBe(true);
  }));

  it('should return the newly created offer', fakeAsync(() => {
    const expectedResponse: Offer = {
      advertisementFormats: [],
      advertisementReview: false,
      assigneeName: "mock",
      bids: [],
      categories: [],
      compensation: component.compensation.value,
      description: component.offerDescription.value,
      expireDate: component.offerExpiryDate.value,
      files: [],
      freeProductSample: false,
      id: 0,
      name: component.offerName.value
    }

    component.offerName.setValue("mockName");
    component.offerDescription.setValue("offerDescription");
    component.offerExpiryDate.setValue(Date.now());
    component.compensation.setValue("mockCompoensation");

    executeCreateOfferRequest(expectedResponse);
  }));

  const executeCreateOfferRequest = (expectedResponse: Offer) => {
    fixture.detectChanges();

    expect(component.offerName.hasError('required') &&
        component.offerExpiryDate.hasError('required') &&
        component.offerDescription.hasError('required') &&
        component.compensation.hasError('required')).toBe(false);

    httpClientSpy.post.and.returnValue(new Observable<Offer>(observer => {
      observer.next(expectedResponse);
      observer.complete();
    }));

    component.createOffer();
    tick(1000);
  };
});
