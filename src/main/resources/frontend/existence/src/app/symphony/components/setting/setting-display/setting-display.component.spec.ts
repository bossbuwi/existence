import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingDisplayComponent } from './setting-display.component';

describe('SettingDisplayComponent', () => {
  let component: SettingDisplayComponent;
  let fixture: ComponentFixture<SettingDisplayComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SettingDisplayComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingDisplayComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
