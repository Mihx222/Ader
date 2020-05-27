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
  });

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
          access_token: 'mock',
          expires_in: 9999,
          jti: 'mock',
          refresh_token: 'mock',
          scope: "mock mock mock",
          token_type: "mock"
        }));

        bidService.getBidByUserEmailAndOfferId('test@test.com', 6).subscribe(
            result => {
              expect(result.offerId).toBe(6);
              expect(result.userEmail).toBe('test@test.com');

              const req = httpMock.expectOne('http:mockland:8080/test/bid/4');
              expect(req.request.method).toEqual('GET');

              req.flush({data: result});
            }
        );
      }
  ));
});
