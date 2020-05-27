import {inject, TestBed} from '@angular/core/testing';

import {AdvertisementFormatService} from './advertisement-format.service';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpTestingController} from "@angular/common/http/testing";

describe('AdvertisementFormatService', () => {
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

    localStorage.setItem('token', JSON.stringify({
      access_token: 'mock',
      expires_in: 999,
      jti: 'mock',
      refresh_token: 'mock',
      scope: "mock mock mock",
      token_type: "mock"
    }));
  });

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [
        HttpClient,
        HttpHandler,
        JwtHelperService,
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS},
        {provide: HttpTestingController, useClass: HttpTestingController}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })

    httpMock = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    const service: AdvertisementFormatService = TestBed.get(AdvertisementFormatService);
    expect(service).toBeTruthy();
  });

  it('expects service to fetch data', inject([HttpTestingController, AdvertisementFormatService],
      (httpMock: HttpTestingController, advertisementFormatService: AdvertisementFormatService) => {
        advertisementFormatService.getAdvertisementFormats().subscribe(
            result => {
              const req = httpMock.expectOne(req => req.method === 'GET' && req.url === 'http:mockland:8080/test/bid/4');
              expect(req.request.method).toEqual('GET');
              expect(result).toBeDefined();
              req.flush({data: result});
            }
        );
      }
  ));
});
