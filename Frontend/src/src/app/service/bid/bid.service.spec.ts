import {inject, TestBed} from '@angular/core/testing';

import {BidService} from './bid.service';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {HttpTestingController} from "@angular/common/http/testing";

describe('BidService', () => {
  let httpMock: HttpTestingController;

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
  })

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        {provide: HttpClient, useClass: HttpClient},
        {provide: HttpHandler, useClass: HttpHandler},
        {provide: JwtHelperService, useClass: JwtHelperService},
        {provide: HttpTestingController, useClass: HttpTestingController},
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ]
    });

    httpMock = TestBed.get(HttpTestingController);
  });

  it('should set an Item', () => {
    localStorage.clear();
    expect(localStorage.setItem('foo', 'bar')).toBeUndefined();
    expect(localStorage.getItem('foo')).toBe('bar'); // bar
  });

  it('should return null for non existing items', () => {
    localStorage.clear();
    expect(localStorage.getItem('foo')).toBeNull(); // Null
  });

  it('should set and remove Item', () => {
    localStorage.clear();
    expect(localStorage.setItem('foo', 'bar')).toBeUndefined(); // undefined
    expect(localStorage.removeItem('foo')).toBeUndefined(); // undefined
    expect(localStorage.getItem('foo')).toBeNull(); // null
  });

  it('should clear the storage', () => {
    localStorage.clear();
    expect(localStorage.setItem('foo', 'bar')).toBeUndefined(); // undefined
    expect(localStorage.setItem('bar', 'foo')).toBeUndefined(); // undefined
    expect(localStorage.clear()).toBeUndefined(); // undefined
    expect(localStorage.getItem('foo')).toBeNull(); // null
    expect(localStorage.getItem('bar')).toBeNull(); // null
  });

  it('should be created', () => {
    const service: BidService = TestBed.get(BidService);
    expect(service).toBeTruthy();
  });

  it('expects service to fetch data', inject([HttpTestingController, BidService],
      (httpMock: HttpTestingController, bidService: BidService) => {
        localStorage.setItem('token', JSON.stringify({
          access_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJleHAiOjIxOTUyMjA4NzgsInVzZXJfbmFtZSI6ImRldkBkZXYuY29tIiwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRFZFUlRJU0VSIiwiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiJiZTMxMTBkMC1hZmYyLTRmODQtODhiMS00ZmUwMjE1YzRiMTIiLCJjbGllbnRfaWQiOiJhZGVyX2FwaSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il19.UVHefCfMR8U09oK7t_T1wcSPAQbf4NUFmAHqwiLQZeM',
          expires_in: 604799999,
          jti: 'be3110d0-aff2-4f84-88b1-4fe0215c4b12',
          refresh_token: 'eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1c2VyX25hbWUiOiJkZXZAZGV2LmNvbSIsInNjb3BlIjpbInJlYWQiLCJ3cml0ZSIsInRydXN0Il0sImF0aSI6ImJlMzExMGQwLWFmZjItNGY4NC04OGIxLTRmZTAyMTVjNGIxMiIsImV4cCI6MjE5NTIyMDg3OCwiYXV0aG9yaXRpZXMiOlsiUk9MRV9BRFZFUlRJU0VSIiwiUk9MRV9BRE1JTiIsIlJPTEVfVVNFUiJdLCJqdGkiOiI0NzQ0YTYzMC01NjI2LTQ2MjQtYmQ0NS01MTUyNjkzZWM4ZWMiLCJjbGllbnRfaWQiOiJhZGVyX2FwaSJ9.7mhSB5w-WMSjWjT1GT2DYd98GHl4R0ocwHP0jMh5aag',
          scope: "read write trust",
          token_type: "bearer"
        }));

        bidService.getBidByUserEmailAndOfferId('user@user.com', 6).subscribe(
            result => {
              expect(result.offerId).toBe(6);
              expect(result.userEmail).toBe('user@user.com');

              const req = httpMock.expectOne('http:localhost:8080/rest/bid/4');
              expect(req.request.method).toEqual('GET');

              req.flush({data: result});
            }
        );
      }
  ));
});
