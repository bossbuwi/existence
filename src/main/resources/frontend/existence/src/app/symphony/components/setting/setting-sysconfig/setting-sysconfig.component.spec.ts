import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingSysconfigComponent } from './setting-sysconfig.component';

describe('SettingSysconfigComponent', () => {
  let component: SettingSysconfigComponent;
  let fixture: ComponentFixture<SettingSysconfigComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SettingSysconfigComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingSysconfigComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
