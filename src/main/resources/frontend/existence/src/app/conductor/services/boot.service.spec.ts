import { TestBed } from '@angular/core/testing';

import { BootService } from './boot.service';

describe('BootService', () => {
  let service: BootService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BootService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
