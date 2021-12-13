import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FatalerrorComponent } from './fatalerror.component';

describe('FatalerrorComponent', () => {
  let component: FatalerrorComponent;
  let fixture: ComponentFixture<FatalerrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FatalerrorComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(FatalerrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
