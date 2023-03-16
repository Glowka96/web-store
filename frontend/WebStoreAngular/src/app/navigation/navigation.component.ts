import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Category } from '../models/category';
import { Product } from '../models/product';
import { ShopService } from '../shop.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private sub: Subscription;
  subcategoryProducts: Product[] = [];

  constructor(private route: ActivatedRoute, private shopService: ShopService) {
    this.sub = shopService.categories.subscribe((categories) => {
      this.categories = categories;
      console.log(this.categories);
    });
  }

  public get getCategories(): Category[] {
    return this.categories;
  }

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      const subcategoryId = params.get('id') as string;
      if (subcategoryId) {
        this.getProductsBySubcategoryId(subcategoryId);
      }
    });
  }

  public getProductsBySubcategoryId(subcategoryId: string) {
    console.log('start gets products');
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        this.subcategoryProducts = products;
      });
    console.log(this.subcategoryProducts);
  }
}
