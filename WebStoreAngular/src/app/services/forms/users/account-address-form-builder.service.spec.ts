import { TestBed } from '@angular/core/testing';

import { AccountAddressFormBuilderService } from './account-address-form-builder.service';

describe('AccountAddressFormBuilderService', () => {
  let service: AccountAddressFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AccountAddressFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
