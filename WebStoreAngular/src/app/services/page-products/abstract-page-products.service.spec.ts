import { TestBed } from '@angular/core/testing';

import { AbstractPageProductsService } from './abstract-page-products.service';

describe('AbstractPageProductsService', () => {
  let service: AbstractPageProductsService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(AbstractPageProductsService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
