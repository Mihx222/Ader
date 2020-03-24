import {TestBed} from '@angular/core/testing';

import {UserSharedDataService} from './user-shared-data.service';

describe('UserSharedDataService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: UserSharedDataService = TestBed.get(UserSharedDataService);
    expect(service).toBeTruthy();
  });
});
