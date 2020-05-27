import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BrowseOffersComponent} from './browse-offers.component';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA} from "@angular/core";
import {MatTableModule} from "@angular/material/table";
import {ActivatedRoute} from "@angular/router";

describe('BrowseOffersComponent', () => {
  let component: BrowseOffersComponent;
  let fixture: ComponentFixture<BrowseOffersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [MatTableModule],
      providers: [
        HttpClient,
        HttpHandler,
        JwtHelperService,
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get(): string {
                  return 'mock';
                },
              },
              data: {offers: 'mock'},
            },
          },
        },
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ],
      declarations: [BrowseOffersComponent],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(BrowseOffersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
