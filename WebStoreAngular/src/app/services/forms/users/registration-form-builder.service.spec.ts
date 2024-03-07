import { TestBed } from '@angular/core/testing';

import { RegistrationFormBuilderService } from './registration-form-builder.service';

describe('RegistrationFormBuilderService', () => {
  let service: RegistrationFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(RegistrationFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
