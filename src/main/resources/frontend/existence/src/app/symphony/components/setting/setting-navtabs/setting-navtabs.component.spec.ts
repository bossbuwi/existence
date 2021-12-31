import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingNavtabsComponent } from './setting-navtabs.component';

describe('SettingNavtabsComponent', () => {
  let component: SettingNavtabsComponent;
  let fixture: ComponentFixture<SettingNavtabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SettingNavtabsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingNavtabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
