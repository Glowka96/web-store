import { Component } from '@angular/core';
import { Subscription } from 'rxjs';
import { PageProductsOptions } from 'src/app/models/products/page-products-options';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { ProductWithPromotion } from 'src/app/models/products/products-with-promotion';
import { ShopService } from 'src/app/services/olders/shop.service';
import { AbstractPageProductsService } from 'src/app/services/page-products/abstract-page-products.service';

@Component({
  selector: 'app-abstract-page-product',
  templateUrl: './abstract-page-product.component.html',
  styleUrls: ['./abstract-page-product.component.scss'],
})
export class AbstractPageProductComponent {
  protected _pageProducts!: PageProductsWithPromotion;
  protected _cart: { [productId: number]: number } = {};
  protected _title!: string;
  protected _selectedPageNumber: number = 1;
  protected _selectedSortBy: string = 'id';
  protected _selectedSortDirection: string = 'asc';
  protected _selectedPageSize: number = 12;
  protected _routeSubscription!: Subscription;

  constructor(
    protected pageProductService: AbstractPageProductsService,
    protected shopService: ShopService
  ) {}

  ngOnInit(): void {}

  ngOnDestroy(): void {
    if (this._routeSubscription) {
      this._routeSubscription.unsubscribe();
    }
  }

  protected getPageProductsWithText(
    text: string,
    pageNo = 0,
    pageSize = 12,
    sortBy = 'id',
    sortDirection = 'asc'
  ) {
    const options = {
      text: text,
      page: pageNo,
      size: pageSize,
      sort: sortBy,
      direction: sortDirection,
    };
    this.getPageProduct(options);
  }

  protected getPageProducts(
    pageNo = 0,
    pageSize = 12,
    sortBy = 'id',
    sortDirection = 'asc'
  ) {
    const options = {
      page: pageNo,
      size: pageSize,
      sort: sortBy,
      direction: sortDirection,
    };
    this.getPageProduct(options);
  }

  private getPageProduct(options: PageProductsOptions) {
    this.pageProductService.getPageProducts(options).subscribe((data) => {
      this._pageProducts = data;
      console.log(data);
      this._pageProducts.products.forEach(
        (product) => (this._cart[product.id] = 1)
      );
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    });
  }

  public getPrice(product: ProductWithPromotion): string {
    if (product.promotionPrice) {
      return (
        Number(this._cart[product.id]) * Number(product.promotionPrice)
      ).toFixed(2);
    }
    return (Number(this._cart[product.id]) * Number(product.price)).toFixed(2);
  }

  protected addToBasket(productId: number) {
    const product = this._pageProducts.products.find(
      (product: ProductWithPromotion) => {
        return product.id == productId;
      }
    );
    if (product) {
      const quantity = this._cart[productId];
      if (quantity < product.quantity) {
        const shipment = { product, quantity };
        this.shopService.addToBasket(shipment);
      }
    }
  }

  protected increaseProductQuantity(product: ProductWithPromotion) {
    this._cart[product.id] >= product.quantity
      ? (this._cart[product.id] = product.quantity)
      : (this._cart[product.id] += 1);
  }

  protected decreaseProductQuantity(product: ProductWithPromotion) {
    this._cart[product.id] > 1
      ? (this._cart[product.id] -= 1)
      : (this._cart[product.id] = 1);
  }

  protected getQuantityOfProduct(productId: number) {
    return this._cart[productId];
  }

  protected changeProductQuantity(
    product: ProductWithPromotion,
    quantity: string
  ) {
    if (Number(quantity) > product.quantity) {
      quantity = String(product.quantity);
    }
    if (Number(quantity) < 1) {
      quantity = '1';
    }
    this._cart[product.id] = Number(quantity);
  }

  protected isMaxQuantityOfProduct(product: ProductWithPromotion): boolean {
    return product.quantity === this._cart[product.id];
  }

  protected isPromotions(product: ProductWithPromotion): boolean {
    return product.promotionPrice ? true : false;
  }

  protected get products(): ProductWithPromotion[] {
    return this?._pageProducts?.products;
  }

  protected get title(): string {
    return this._title ? this._title.toUpperCase() : '';
  }

  protected get quantityOfProducts(): number {
    return this?._pageProducts?.totalElements;
  }

  protected get listSortBy() {
    return this?._pageProducts?.sortByTypes;
  }

  protected get listSortDirection() {
    return this?._pageProducts?.sortDirectionTypes;
  }

  protected get totalPagesArray(): number[] {
    return Array.from(
      { length: this?._pageProducts?.totalPages },
      (_, index) => index + 1
    );
  }
}
