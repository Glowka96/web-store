import { TestBed } from '@angular/core/testing';

import { LoginFormBuilderService } from './login-form-builder.service';

describe('LoginFormBuilderService', () => {
  let service: LoginFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
