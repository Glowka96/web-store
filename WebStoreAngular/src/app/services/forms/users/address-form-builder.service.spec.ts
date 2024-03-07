import { TestBed } from '@angular/core/testing';

import { AddressFormBuilderService } from './address-form-builder.service';

describe('AddressFormBuilderService', () => {
  let service: AddressFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AddressFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
