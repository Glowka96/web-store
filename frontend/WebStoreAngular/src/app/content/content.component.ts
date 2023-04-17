import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Product } from '../models/product';
import { ShopService } from '../services/shop.service';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss'],
})
export class ContentComponent implements OnInit {
  private subcategoryProducts: Product[] = [];
  private subcatName!: string;
  private countProducts!: number;
  private subcategoryId!: string;
  private countPage!: number;

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      this.subcategoryId = params.get('id') as string;
      if (this.subcategoryId) {
        this.getProductsBySubcategoryId(this.subcategoryId);
        this.getCountProductsBySubcategoryId(this.subcategoryId);
      }
      this.subcatName = params.get('subcategoryName') as string;
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        products.forEach((product) => (product.amountOfProduct = 1));
        this.subcategoryProducts = products;
        console.log(this.subcategoryProducts);
      });
  }

  public getPageProductsBySubcatId(page: number) {
    this.shopService
      .getProductsBySubcategory(this.subcategoryId, page)
      .subscribe((products) => {
        products.forEach((product) => (product.amountOfProduct = 1));
        this.subcategoryProducts = products;
        console.log(this.subcategoryProducts);
      });
  }

  private getCountProductsBySubcategoryId(subcategoryId: string) {
    this.shopService.getCountProducts(subcategoryId).subscribe((value) => {
      this.countProducts = value;
      this.countPage = Math.ceil(value / 12);
    });
  }

  public get products(): Product[] {
    return this.subcategoryProducts;
  }

  public get subcategoryName(): string {
    return this.subcatName.toUpperCase();
  }

  public get countProduct(): number {
    return this.countProducts;
  }

  public get countPagesArray(): number[] {
    return Array.from({ length: this.countPage }, (_, i) => i);
  }
}
