import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SettingUsersComponent } from './setting-users.component';

describe('SettingUsersComponent', () => {
  let component: SettingUsersComponent;
  let fixture: ComponentFixture<SettingUsersComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SettingUsersComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SettingUsersComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
