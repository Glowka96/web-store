import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { ProductResponse } from 'src/app/models/product-response';
import { ProductService } from 'src/app/services/product.service';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  private products: ProductResponse[] = [];
  private cart: { [productId: string]: number } = {};
  private title!: string;
  private quantityOfProducts!: number;
  private subcategoryId!: string;
  private countPage!: number;
  private searchQuery!: string;
  private pageClicked = false;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this.subcategoryId = params.get('id') as string;
        this.getProductsBySubcategoryId(this.subcategoryId);
        this.getQuantityOfProductsBySubcategoryId(this.subcategoryId);
        this.title = params.get('subcategoryName') as string;
      }
    });
    this.route.queryParams.subscribe((params: Params) => {
      if (params['q'] && !this.pageClicked) {
        this.searchQuery = params['q'];
        this.getSearchProducts(this.searchQuery);
        this.getQuanititySearchProducts(this.searchQuery);
        this.title = 'Result of search';
      }
      this.pageClicked = false;
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    this.productService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        products.forEach((product) => (this.cart[product.id] = 1));
        this.products = products;
      });
  }

  private getQuantityOfProductsBySubcategoryId(subcategoryId: string) {
    this.productService
      .getQuantityOfProducts(subcategoryId)
      .subscribe((value) => {
        this.quantityOfProducts = value;
        this.countPage = Math.ceil(value / 12);
      });
  }

  private getSearchProducts(text: string) {
    this.productService.getSearchProducts(text).subscribe((products) => {
      products.forEach((product) => (this.cart[product.id] = 1));
      this.products = products;
    });
  }

  private getQuanititySearchProducts(text: string) {
    this.productService.getQuanititySearchProducts(text).subscribe((value) => {
      this.quantityOfProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  public getPageProducts(page: number) {
    this.pageClicked = true;
    if (this.title.match('search')) {
      this.productService
        .getSearchProducts(this.searchQuery, page)
        .subscribe((products) => {
          products.forEach((product) => (this.cart[product.id] = 1));
          this.products = products;
        });
    } else {
      this.productService
        .getProductsBySubcategory(this.subcategoryId, page)
        .subscribe((products) => {
          products.forEach((product) => (this.cart[product.id] = 1));
          this.products = products;
        });
    }
  }

  public addToBasket(id: string) {
    const product = this.products.find((product: ProductResponse) => {
      return product.id == id;
    });
    if (product) {
      const price = this.getPrice(product);
      const quantity = this.cart[product.id];
      const shipment = { product, quantity, price };
      this.shopService.addToBasket(shipment);
    }
  }

  public getPrice(product: ProductResponse): string {
    return (Number(this.cart[product.id]) * Number(product.price)).toFixed(2);
  }

  public getProductQuantity(productId: string) {
    return this.cart[productId] || 1;
  }

  public changeProductQuantity(productId: string, quantity: string) {
    if (Number(quantity) > 100) {
      quantity = '100';
    }
    if (Number(quantity) < 1) {
      quantity = '1';
    }
    this.cart[productId] = Number(quantity);
  }

  public increaseProductQuantity(productId: string) {
    if (this.cart[productId] == 100) {
      this.cart[productId] = 100;
    } else {
      this.cart[productId] += 1;
    }
  }

  public decreaseProductQuantity(productId: string) {
    if (this.cart[productId] > 1) {
      this.cart[productId] -= 1;
    } else {
      this.cart[productId] = 1;
    }
  }

  public get listProduct(): ProductResponse[] {
    return this.products;
  }

  public get getTitle(): string {
    return this.title.toUpperCase();
  }

  public get quantityOfProduct(): number {
    return this.quantityOfProducts;
  }

  public get countPagesArray(): number[] {
    return Array.from({ length: this.countPage }, (_, i) => i);
  }
}