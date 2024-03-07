import { TestBed } from '@angular/core/testing';

import { DeliveryTypeService } from './delivery-type.service';

describe('DeliveryTypeService', () => {
  let service: DeliveryTypeService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(DeliveryTypeService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
