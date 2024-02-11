import { TestBed } from '@angular/core/testing';

import { AccountFormBuilderService } from './account-form-builder.service';

describe('AccountFormBuilderService', () => {
  let service: AccountFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
