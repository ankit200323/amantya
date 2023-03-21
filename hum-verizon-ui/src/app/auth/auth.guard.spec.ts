import { TestBed } from '@angular/core/testing';

import { AuthGuard } from './auth.guard';

describe('AuthGuard', () => {
  let guard: AuthGuard;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    guard = TestBed.inject(AuthGuard);
  });

  it('should be created', () => {
    expect(guard).toBeTruthy();
  });

  it('should call function checkTokenExpiration',()=>{
    expect(guard.checkTokenExpiration()).toBe(false);
  })
  
  it('should call function getAuthStatus else condition',()=>{
    expect(guard.getAuthStatus(false)).toBe(false);
  })

  it('should call function getAuthStatus if condition',()=>{
    expect(guard.getAuthStatus(true)).toBe(true);
  })

});
