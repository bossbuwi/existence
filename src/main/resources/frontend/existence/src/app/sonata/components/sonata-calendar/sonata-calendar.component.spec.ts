import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataCalendarComponent } from './sonata-calendar.component';

describe('SonataCalendarComponent', () => {
  let component: SonataCalendarComponent;
  let fixture: ComponentFixture<SonataCalendarComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataCalendarComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataCalendarComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
