import { TestBed } from '@angular/core/testing';

import { PromotionProductsService } from './promotion-products.service';

describe('PromotionProductsService', () => {
  let service: PromotionProductsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromotionProductsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
