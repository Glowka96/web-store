import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PagePromotionsProductsComponent } from './page-promotion-products.component';

describe('PagePromotionsProductsComponent', () => {
  let component: PagePromotionsProductsComponent;
  let fixture: ComponentFixture<PagePromotionsProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PagePromotionsProductsComponent],
    });
    fixture = TestBed.createComponent(PagePromotionsProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
