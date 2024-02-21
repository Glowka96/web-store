import { TestBed } from '@angular/core/testing';

import { ProductFromBuilderService } from './product-from-builder.service';

describe('ProductFromBuilderService', () => {
  let service: ProductFromBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ProductFromBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
