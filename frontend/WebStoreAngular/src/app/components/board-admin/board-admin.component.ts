import { Component, OnInit } from '@angular/core';
import { Category } from 'src/app/models/category';
import { Subcategory } from 'src/app/models/subcategory';
import { CategoryService } from 'src/app/services/category.service';
import { ShopService } from 'src/app/services/shop.service';
import { SubcategoryService } from 'src/app/services/subcategory.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.scss'],
})
export class BoardAdminComponent implements OnInit {
  private categories: Category[] = [];
  private subcategories: Subcategory[] = [];

  constructor(
    private shopService: CategoryService,
    private subcategoryService: SubcategoryService
  ) {
    shopService.categories$.subscribe((categories) => {
      this.categories = categories;
    });
    subcategoryService.subcategories$.subscribe((subcategories) => {
      this.subcategories = subcategories;
    });
  }

  ngOnInit(): void {}

  public get listCategories() {
    return this.categories;
  }

  public get listSubcategories() {
    return this.subcategories;
  }
}
