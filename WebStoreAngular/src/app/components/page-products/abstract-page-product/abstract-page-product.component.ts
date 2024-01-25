import { Component } from '@angular/core';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { ProductWithPromotion } from 'src/app/models/products/products-with-promotion';
import { AbstractPageProductsService } from 'src/app/services/page-products/abstract-page-products.service';

@Component({
  selector: 'app-abstract-page-product',
  templateUrl: './abstract-page-product.component.html',
  styleUrls: ['./abstract-page-product.component.scss'],
})
export class AbstractPageProductComponent {
  protected pageProduct!: PageProductsWithPromotion;
  protected pageTitle!: string;

  constructor(protected pageProductService: AbstractPageProductsService) {
    console.log('in abstract constr');
  }

  ngOnInit(): void {}

  protected get listProduct(): ProductWithPromotion[] {
    return this.pageProduct.products;
  }

  protected get title(): string {
    return this.pageTitle ? this.pageTitle.toUpperCase() : '';
  }

  protected get quantityOfProducts(): number {
    return this.pageProduct.totalElements;
  }

  protected get totalPagesArray(): number[] {
    return Array.from(
      { length: this.pageProduct.totalPages },
      (_, index) => index + 1
    );
  }
}
