import {TestBed} from '@angular/core/testing';

import {UserSharedDataService} from './user-shared-data.service';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";

describe('UserSharedDataService', () => {
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
    const service: UserSharedDataService = TestBed.get(UserSharedDataService);
    expect(service).toBeTruthy();
  });
});
