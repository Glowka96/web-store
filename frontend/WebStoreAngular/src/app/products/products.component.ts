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

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const subcategoryId = params.get('id') as string;
      this.getProductsBySubcategoryId(subcategoryId);
    });
  }

  getProductsBySubcategoryId(subcategoryId: string) {
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        this.subcategoryProducts = products;
      });
  }
}
