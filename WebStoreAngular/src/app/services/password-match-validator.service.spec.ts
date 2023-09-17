import { TestBed } from '@angular/core/testing';

import { PasswordMatchValidatorService } from './password-match-validator.service';

describe('PasswordMatchValidatorService', () => {
  let service: PasswordMatchValidatorService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PasswordMatchValidatorService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
