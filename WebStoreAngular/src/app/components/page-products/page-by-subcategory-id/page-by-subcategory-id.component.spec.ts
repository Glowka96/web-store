import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageBySubcategoryId } from './page-by-subcategory-id.component';

describe('PageProductsComponent', () => {
  let component: PageBySubcategoryId;
  let fixture: ComponentFixture<PageBySubcategoryId>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageBySubcategoryId],
    });
    fixture = TestBed.createComponent(PageBySubcategoryId);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
