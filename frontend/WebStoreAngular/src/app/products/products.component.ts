import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Product } from '../models/product';
import { ShopService } from '../shop.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  private subcategoryProducts: Product[] = [];
  private sub: Subscription | undefined;
  private products: Product[] = [];

  constructor(private route: ActivatedRoute, private shopService: ShopService) {
    this.sub = this.shopService.products?.subscribe((product: Product[]) => {
      this.products = product;
      console.log(this.products);
    });
  }

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const subcategoryId = params.get('id') as string;
      console.log(subcategoryId);
      if (subcategoryId) {
        this.getProductsBySubcategoryId(subcategoryId);
      }
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    console.log('start gets products2');
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        this.subcategoryProducts = products;
        console.log(this.subcategoryProducts);
      });
  }

  public get getProducts(): Product[] {
    return this.subcategoryProducts;
  }
}
