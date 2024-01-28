import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageBySubcategoryProducts } from './page-by-subcategory-products.component';

describe('PageProductsComponent', () => {
  let component: PageBySubcategoryProducts;
  let fixture: ComponentFixture<PageBySubcategoryProducts>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageBySubcategoryProducts],
    });
    fixture = TestBed.createComponent(PageBySubcategoryProducts);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
