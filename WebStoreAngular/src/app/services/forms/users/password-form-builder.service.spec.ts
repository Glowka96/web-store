import { TestBed } from '@angular/core/testing';

import { PasswordFormBuilderService } from './password-form-builder.service';

describe('PasswordFormControlService', () => {
  let service: PasswordFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PasswordFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
