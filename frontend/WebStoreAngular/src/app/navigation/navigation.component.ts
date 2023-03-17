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

  constructor(private route: ActivatedRoute, private shopService: ShopService) {
    this.sub = shopService.categories.subscribe((categories) => {
      this.categories = categories;
    });
  }

  public get getCategories(): Category[] {
    return this.categories;
  }

  ngOnInit() {}
}
