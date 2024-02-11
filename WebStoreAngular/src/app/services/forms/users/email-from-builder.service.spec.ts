import { TestBed } from '@angular/core/testing';

import { EmailFromBuilderService } from './email-from-builder.service';

describe('EmailFromBuilderService', () => {
  let service: EmailFromBuilderService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(EmailFromBuilderService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
