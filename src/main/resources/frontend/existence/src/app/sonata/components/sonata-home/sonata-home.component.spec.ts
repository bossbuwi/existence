import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataHomeComponent } from './sonata-home.component';

describe('SonataHomeComponent', () => {
  let component: SonataHomeComponent;
  let fixture: ComponentFixture<SonataHomeComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataHomeComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataHomeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
