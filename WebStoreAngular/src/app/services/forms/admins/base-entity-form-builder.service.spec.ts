import { TestBed } from '@angular/core/testing';

import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';

describe('BaseEntityFormBuilderService', () => {
  let service: BaseEntityFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(BaseEntityFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
