import { TestBed } from '@angular/core/testing';

import { DeliveryTypeFormBuilderService } from './delivery-type-form-builder.service';

describe('DeliveryTypeFormBuilderService', () => {
  let service: DeliveryTypeFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryTypeFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
