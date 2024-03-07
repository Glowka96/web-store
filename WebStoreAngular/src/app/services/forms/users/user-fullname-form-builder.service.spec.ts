import { TestBed } from '@angular/core/testing';

import { UserFullnameFormBuilderService as UserFullnameFormBuilderService } from './user-fullname-form-builder.service';

describe('UserNameFormBuilderService', () => {
  let service: UserFullnameFormBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserFullnameFormBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
