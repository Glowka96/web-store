import { TestBed } from '@angular/core/testing';

import { PromotionFormBuilderService } from './promotion-form-builder.service';

describe('PromotionFormBuilderService', () => {
  let service: PromotionFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(PromotionFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
