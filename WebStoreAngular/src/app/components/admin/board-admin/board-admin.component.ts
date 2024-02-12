import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { CategoryResponse } from 'src/app/models/products/category-response';
import { ProducerResponse } from 'src/app/models/products/producer-response';
import { ProductTypeResponse } from 'src/app/models/products/product-type-response';
import { SubcategoryResponse } from 'src/app/models/products/subcategory-response';
import { CategoryService } from 'src/app/services/products/category.service';
import { ProducerService } from 'src/app/services/products/producer.service';
import { ProductTypeService } from 'src/app/services/products/product-type.service';
import { SubcategoryService } from 'src/app/services/products/subcategory.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.scss'],
})
export class BoardAdminComponent implements OnInit {
  private _categories: CategoryResponse[] = [];
  private _subcategories: SubcategoryResponse[] = [];
  private _producers: ProducerResponse[] = [];
  private _productTypes: ProductTypeResponse[] = [];
  private loggedRole!: string;

  constructor(
    private categoryService: CategoryService,
    private subcategoryService: SubcategoryService,
    private producerService: ProducerService,
    private productTypeService: ProductTypeService,
    private router: Router
  ) {
    this.loggedRole = sessionStorage.getItem('role')!;
    categoryService.categories$
      .pipe(take(1))
      .subscribe((categories) => (this._categories = categories));
    subcategoryService.subcategories$
      .pipe(take(1))
      .subscribe((subcategories) => (this._subcategories = subcategories));
    producerService.producers$
      .pipe(take(1))
      .subscribe((producers) => (this._producers = producers));
    productTypeService.productTypes$
      .pipe(take(1))
      .subscribe((productTypes) => (this._productTypes = productTypes));
  }

  ngOnInit(): void {
    if (!this.isAdmin()) {
      this.router.navigate([''], {});
    }
  }

  public isAdmin() {
    return this.loggedRole === 'ROLE_ADMIN';
  }

  public get categories() {
    return this._categories;
  }

  public get subcategories() {
    return this._subcategories;
  }

  public get producers() {
    return this._producers;
  }

  public get productTypes() {
    return this._productTypes;
  }
}
