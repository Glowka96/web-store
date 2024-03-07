import { TestBed } from '@angular/core/testing';

import { SubcategoryFormBuilderService } from './subcategory-form-builder.service';

describe('SubcategoryFormBuilderService', () => {
  let service: SubcategoryFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(SubcategoryFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
