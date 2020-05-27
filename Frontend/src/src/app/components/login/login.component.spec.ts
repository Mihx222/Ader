import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {LoginComponent} from './login.component';
import {HttpClient, HttpHandler} from "@angular/common/http";
import {JWT_OPTIONS, JwtHelperService} from "@auth0/angular-jwt";
import {CUSTOM_ELEMENTS_SCHEMA, DebugElement, NO_ERRORS_SCHEMA} from "@angular/core";
import {ActivatedRoute, Router} from "@angular/router";
import {By} from "@angular/platform-browser";

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let debugElement: DebugElement;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        HttpClient,
        {
          provide: Router, useClass: class {
            navigate = jasmine.createSpy("navigate");
          }
        },
        {
          provide: ActivatedRoute,
          useValue: {
            snapshot: {
              paramMap: {
                get(): string {
                  return 'mock';
                },
              },
              queryParams: {returnUrl: 'mock'},
            },
          },
        },
        HttpHandler,
        JwtHelperService,
        {provide: JWT_OPTIONS, useValue: JWT_OPTIONS}
      ],
      schemas: [CUSTOM_ELEMENTS_SCHEMA, NO_ERRORS_SCHEMA]
    })
        .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    debugElement = fixture.debugElement.query(By.css('form'));
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('login button should be disabled when form is empty', async(() => {
    debugElement = fixture.debugElement.query(By.css('#loginButton'));
    expect(debugElement.nativeElement.disabled).toBe(true);
  }));

  it('login button should be enabled when form is completed', async(() => {
    component.emailFormControl.setValue('mock');
    component.passwordFormControl.setValue('mock');
    fixture.detectChanges();

    expect(component.emailFormControl.hasError('required')).toBe(false);
    expect(component.passwordFormControl.hasError('required')).toBe(false);
  }));

  it('login button should be disabled when email is incorrect', async(() => {
    component.emailFormControl.setValue('mock');
    component.passwordFormControl.setValue('mock');
    fixture.detectChanges();

    expect(component.emailFormControl.hasError('email')).toBe(true);
    expect(component.passwordFormControl.hasError('required')).toBe(false);

    debugElement = fixture.debugElement.query(By.css('#loginButton'));
    expect(debugElement.nativeElement.disabled).toBe(true);
  }));
});
