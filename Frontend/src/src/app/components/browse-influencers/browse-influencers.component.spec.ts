import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {BrowseInfluencersComponent} from './browse-influencers.component';

describe('BrowseInfluencersComponent', () => {
  let component: BrowseInfluencersComponent;
  let fixture: ComponentFixture<BrowseInfluencersComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [BrowseInfluencersComponent]
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
