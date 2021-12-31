import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataNavtabsComponent } from './sonata-navtabs.component';

describe('SonataNavtabsComponent', () => {
  let component: SonataNavtabsComponent;
  let fixture: ComponentFixture<SonataNavtabsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataNavtabsComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataNavtabsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
