import { TestBed } from '@angular/core/testing';

import { SubcategoryIdProductsService } from './subcategory-id-products.service';

describe('SubcategoryIdProductsService', () => {
  let service: SubcategoryIdProductsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubcategoryIdProductsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
