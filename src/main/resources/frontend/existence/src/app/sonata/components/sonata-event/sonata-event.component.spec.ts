import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SonataEventComponent } from './sonata-event.component';

describe('SonataEventComponent', () => {
  let component: SonataEventComponent;
  let fixture: ComponentFixture<SonataEventComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ SonataEventComponent ]
    })
    .compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(SonataEventComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
