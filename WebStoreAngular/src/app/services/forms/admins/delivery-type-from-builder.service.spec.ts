import { TestBed } from '@angular/core/testing';

import { DeliveryTypeFromBuilderService } from './delivery-type-from-builder.service';

describe('DeliveryTypeFromBuilderService', () => {
  let service: DeliveryTypeFromBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryTypeFromBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
