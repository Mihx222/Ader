import {inject, TestBed} from '@angular/core/testing';

import {UserService} from './user.service';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";
import {HttpTestingController} from "@angular/common/http/testing";

describe('UserService', () => {
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
      expires_in: 9999,
      jti: 'mock',
      refresh_token: 'mock',
      scope: "mock mock mock",
      token_type: "mock"
    }));
  });

  beforeEach(() => TestBed.configureTestingModule({
    providers: [
      HttpClient,
      HttpHandler,
      JwtHelperService,
      {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
  }));

  it('should be created', () => {
    const service: UserService = TestBed.get(UserService);
    expect(service).toBeTruthy();
  });

  it('expects service to fetch data', inject([HttpTestingController, UserService],
      (httpMock: HttpTestingController, userService: UserService) => {
        userService.getUser('test@test.com').subscribe(
            result => {
              expect(result.email).toBe('test@test.com');

              const req = httpMock.expectOne('http:mockland:8080/test/user/test@test.com');
              expect(req.request.method).toEqual('GET');

              req.flush({data: result});
            }
        );
      }
  ));
});
