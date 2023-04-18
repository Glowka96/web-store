import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Params, Router } from '@angular/router';
import { Product } from '../models/product';
import { ShopService } from '../services/shop.service';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss'],
})
export class ContentComponent implements OnInit {
  private products: Product[] = [];
  private title!: string;
  private countProducts!: number;
  private subcategoryId!: string;
  private countPage!: number;
  private searchQuery!: string; // Add searchQuery variable

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
      if (params['q']) {
        this.searchQuery = params['q'];
        this.searchProducts(this.searchQuery);
        this.getAmountSearchProducts(this.searchQuery);
        this.title = 'Result of search';
      }
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

  private searchProducts(text: string) {
    this.shopService.getSearchProducts(text).subscribe((products) => {
      products.forEach((product) => (product.amountOfProduct = 1));
      this.products = products;
    });
  }

  private getAmountSearchProducts(text: string) {
    this.shopService.getCountSearchProducts(text).subscribe((value) => {
      this.countProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  public getPageProducts(page: number) {
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
