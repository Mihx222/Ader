import {TestBed} from '@angular/core/testing';

import {AdvertisementFormatService} from './advertisement-format.service';

describe('AdvertisementFormatService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: AdvertisementFormatService = TestBed.get(AdvertisementFormatService);
    expect(service).toBeTruthy();
  });
});
