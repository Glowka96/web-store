import { ComponentFixture, TestBed } from '@angular/core/testing';

import { PageNewProductsComponent } from './page-new-products.component';

describe('PageNewProductsComponent', () => {
  let component: PageNewProductsComponent;
  let fixture: ComponentFixture<PageNewProductsComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [PageNewProductsComponent]
    });
    fixture = TestBed.createComponent(PageNewProductsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
