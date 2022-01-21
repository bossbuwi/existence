import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataSystemsComponent } from './sonata-systems.component';

describe('SonataSystemsComponent', () => {
  let component: SonataSystemsComponent;
  let fixture: ComponentFixture<SonataSystemsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataSystemsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataSystemsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
