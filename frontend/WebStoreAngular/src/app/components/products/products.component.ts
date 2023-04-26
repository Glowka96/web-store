import { Component, Input, OnInit } from '@angular/core';
import { ActivatedRoute, Params } from '@angular/router';
import { Product } from 'src/app/models/product';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  //@Input()
  //products: Product[] = [];
  private products: Product[] = [];
  private title!: string;
  private countProducts!: number;
  private subcategoryId!: string;
  private countPage!: number;
  private searchQuery!: string;
  private pageClicked: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this.subcategoryId = params.get('id') as string;
        this.getProductsBySubcategoryId(this.subcategoryId);
        this.getAmountProductsBySubcategoryId(this.subcategoryId);
        this.title = params.get('subcategoryName') as string;
      }
    });
    this.route.queryParams.subscribe((params: Params) => {
      if (params['q'] && !this.pageClicked) {
        this.searchQuery = params['q'];
        this.getSearchProducts(this.searchQuery);
        this.getAmountSearchProducts(this.searchQuery);
        this.title = 'Result of search';
      }
      this.pageClicked = false;
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        products.forEach((product) => (product.amountOfProduct = 1));
        this.products = products;
      });
  }

  private getAmountProductsBySubcategoryId(subcategoryId: string) {
    this.shopService.getCountProducts(subcategoryId).subscribe((value) => {
      this.countProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  private getSearchProducts(text: string) {
    this.shopService.getSearchProducts(text).subscribe((products) => {
      console.log('start get products');
      products.forEach((product) => (product.amountOfProduct = 1));
      this.products = products;
    });
  }

  private getAmountSearchProducts(text: string) {
    this.shopService.getCountSearchProducts(text).subscribe((value) => {
      console.log('start get amount products');
      this.countProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  public getPageProducts(page: number) {
    this.pageClicked = true;
    if (this.title.match('search')) {
      this.shopService
        .getSearchProducts(this.searchQuery, page)
        .subscribe((products) => {
          products.forEach((product) => (product.amountOfProduct = 1));
          this.products = products;
        });
    } else {
      this.shopService
        .getProductsBySubcategory(this.subcategoryId, page)
        .subscribe((products) => {
          products.forEach((product) => (product.amountOfProduct = 1));
          this.products = products;
        });
    }
  }

  public increaseNumberOfProduct(product: Product): void {
    if (product.amountOfProduct < 100) {
      product.amountOfProduct++;
    }
  }

  public decreaseNumberOfProduct(product: Product): void {
    if (product.amountOfProduct > 1) {
      product.amountOfProduct--;
    }
  }

  public getPrice(product: Product): string {
    return (parseFloat(product.price) * product.amountOfProduct).toFixed(2);
  }

  public get listProduct(): Product[] {
    return this.products;
  }

  public get getTitle(): string {
    return this.title.toUpperCase();
  }

  public get countProduct(): number {
    return this.countProducts;
  }

  public get countPagesArray(): number[] {
    return Array.from({ length: this.countPage }, (_, i) => i);
  }
}
