import { TestBed } from '@angular/core/testing';

import { AuthinterceptorService } from './authinterceptor.service';

describe('AuthinterceptorService', () => {
  let service: AuthinterceptorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AuthinterceptorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
