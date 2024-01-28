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
  protected _pageProducts!: PageProductsWithPromotion;
  protected _title!: string;
  protected _selectedPageNumber: number = 1;
  protected _selectedSortBy: string = 'id';
  protected _selectedSortDirection: string = 'asc';
  protected _selectedPageSize: number = 12;

  constructor(protected pageProductService: AbstractPageProductsService) {}

  ngOnInit(): void {}

  protected get products(): ProductWithPromotion[] {
    return this._pageProducts.products;
  }

  protected get title(): string {
    return this._title ? this._title.toUpperCase() : '';
  }

  protected setTitle(title: string) {
    this._title = title;
  }

  protected get quantityOfProducts(): number {
    return this._pageProducts.totalElements;
  }

  protected get listSortBy() {
    return this._pageProducts.sortByTypes;
  }

  protected get listSortDirection() {
    return this._pageProducts.sortDirectionTypes;
  }

  protected get totalPagesArray(): number[] {
    return Array.from(
      { length: this._pageProducts.totalPages },
      (_, index) => index + 1
    );
  }
}
