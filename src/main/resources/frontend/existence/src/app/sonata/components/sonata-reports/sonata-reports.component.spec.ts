import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataReportsComponent } from './sonata-reports.component';

describe('SonataReportsComponent', () => {
  let component: SonataReportsComponent;
  let fixture: ComponentFixture<SonataReportsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataReportsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataReportsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
