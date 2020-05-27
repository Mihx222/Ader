import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BrowseInfluencersComponent} from './browse-influencers.component';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";

describe('BrowseInfluencersComponent', () => {
  let component: BrowseInfluencersComponent;
  let fixture: ComponentFixture<BrowseInfluencersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BrowseInfluencersComponent],
      providers: [
        HttpClient,
        HttpHandler,
        JwtHelperService,
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseInfluencersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
