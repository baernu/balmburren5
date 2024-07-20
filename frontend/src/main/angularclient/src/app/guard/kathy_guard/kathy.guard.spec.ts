import { TestBed } from '@angular/core/testing';

import { KathyGuard } from './kathy.guard';

describe('KathyGuard', () => {
  let guard: KathyGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(KathyGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });
});
