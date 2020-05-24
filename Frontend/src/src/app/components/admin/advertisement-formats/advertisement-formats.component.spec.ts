import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { AdvertisementFormatsComponent } from './advertisement-formats.component';

describe('AdvertisementFormatsComponent', () => {
  let component: AdvertisementFormatsComponent;
  let fixture: ComponentFixture<AdvertisementFormatsComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ AdvertisementFormatsComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(AdvertisementFormatsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
