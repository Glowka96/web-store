import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { Subscription, take } from 'rxjs';
import { CategoryResponse } from 'src/app/models/products/category-response';
import { ProducerResponse } from 'src/app/models/products/producer-response';
import { ProductResponse } from 'src/app/models/products/product-response';
import { ProductTypeResponse } from 'src/app/models/products/product-type-response';
import { SubcategoryResponse } from 'src/app/models/products/subcategory-response';
import { CategoryService } from 'src/app/services/products/category.service';
import { ProducerService } from 'src/app/services/products/producer.service';
import { ProductTypeService } from 'src/app/services/products/product-type.service';
import { ProductService } from 'src/app/services/products/product.service';
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
  private _products: ProductResponse[] = [];

  constructor(
    private categoryService: CategoryService,
    private subcategoryService: SubcategoryService,
    private producerService: ProducerService,
    private productTypeService: ProductTypeService,
    private productService: ProductService,
    private router: Router
  ) {
    categoryService
      .getCategories()
      .pipe(take(1))
      .subscribe((categories) => (this._categories = categories));
    subcategoryService
      .getAllSubcategories()
      .pipe(take(1))
      .subscribe((subcategories) => (this._subcategories = subcategories));
    producerService
      .getAllProducers()
      .pipe(take(1))
      .subscribe((producers) => (this._producers = producers));
    productTypeService
      .getAllProductTypes()
      .pipe(take(1))
      .subscribe((productTypes) => (this._productTypes = productTypes));
    productService
      .getAllProducts()
      .pipe(take(1))
      .subscribe((products) => (this._products = products));
  }

  ngOnInit(): void {
    const role = sessionStorage.getItem('role');
    if (role !== 'ROLE_ADMIN') {
      this.router.navigate([''], {});
    }
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

  public get products() {
    return this._products;
  }
}
