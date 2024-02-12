import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { CategoryResponse } from 'src/app/models/products/category-response';
import { SubcategoryResponse } from 'src/app/models/products/subcategory-response';
import { CategoryService } from 'src/app/services/products/category.service';
import { SubcategoryService } from 'src/app/services/products/subcategory.service';

@Component({
  selector: 'app-board-admin',
  templateUrl: './board-admin.component.html',
  styleUrls: ['./board-admin.component.scss'],
})
export class BoardAdminComponent implements OnInit {
  private categories: CategoryResponse[] = [];
  private subcategories: SubcategoryResponse[] = [];
  private loggedRole!: string;

  constructor(
    private shopService: CategoryService,
    private subcategoryService: SubcategoryService,
    private router: Router
  ) {
    this.loggedRole = sessionStorage.getItem('role')!;
    shopService.categories$.pipe(take(1)).subscribe((categories) => {
      this.categories = categories;
    });
    subcategoryService.subcategories$
      .pipe(take(1))
      .subscribe((subcategories) => {
        this.subcategories = subcategories;
      });
  }

  ngOnInit(): void {
    if (!this.isAdmin()) {
      this.router.navigate([''], {});
    }
  }

  public isAdmin() {
    return this.loggedRole === 'ROLE_ADMIN';
  }

  public get listCategories() {
    return this.categories;
  }

  public get listSubcategories() {
    return this.subcategories;
  }
}
