import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {NewOfferComponent} from './new-offer.component';
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";
import {Router} from "@angular/router";
import {HttpClientTestingModule} from "@angular/common/http/testing";
import {RouterTestingModule} from "@angular/router/testing";

describe('NewOfferComponent', () => {
  let component: NewOfferComponent;
  let fixture: ComponentFixture<NewOfferComponent>;

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
      access_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjIxOTUyMjA4NzgsInVzZXJfbmFtZSI6ImRldkBkZXYuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRFZFUlRJU0VSIiwiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJiZTMxMTBkMC1hZmYyLTRmODQtODhiMS00ZmUwMjE1YzRiMTIiLCJjbGllbnRfaWQiOiJhZGVyX2FwaSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il19.UVHefCfMR8U09oK7t_T1wcSPAQbf4NUFmAHqwiLQZeM',
      expires_in: 604799999,
      jti: 'be3110d0-aff2-4f84-88b1-4fe0215c4b12',
      refresh_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkZXZAZGV2LmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImF0aSI6ImJlMzExMGQwLWFmZjItNGY4NC04OGIxLTRmZTAyMTVjNGIxMiIsImV4cCI6MjE5NTIyMDg3OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRFZFUlRJU0VSIiwiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI0NzQ0YTYzMC01NjI2LTQ2MjQtYmQ0NS01MTUyNjkzZWM4ZWMiLCJjbGllbnRfaWQiOiJhZGVyX2FwaSJ9.7mhSB5w-WMSjWjT1GT2DYd98GHl4R0ocwHP0jMh5aag',
      scope: "read write trust",
      token_type: "bearer"
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
  })

  beforeEach(async(() => {
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
});
