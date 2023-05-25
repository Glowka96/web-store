import { Component, OnInit } from '@angular/core';

import {
  ControlContainer,
  FormControl,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import { CategoryResponse } from 'src/app/models/category-response';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { FormLoginService } from 'src/app/services/form-login.service';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
  viewProviders: [
    {
      provide: ControlContainer,
      useExisting: FormGroupDirective,
    },
  ],
})
export class NavigationComponent implements OnInit {
  private categories: CategoryResponse[] = [];
  private loggedIn: boolean = false;

  public searchForm = new FormGroup({
    search: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  constructor(
    private productService: ProductService,
    private formLoginService: FormLoginService,
    private authService: AuthenticationService,
    private categoryService: CategoryService
  ) {
    categoryService.categories$.subscribe((categories) => {
      this.categories = categories;
      console.log(categories);
    });
    authService.loggedIn$().subscribe((value) => {
      this.loggedIn = value;
    });
  }

  ngOnInit() {}

  public get getCategories(): CategoryResponse[] {
    return this.categories;
  }

  public isLoggedIn(): boolean {
    return this.loggedIn;
  }

  public logout(): void {
    this.authService.logout();
  }

  public onSearch(): void {
    if (this.searchForm.valid) {
      let text = this.searchForm.controls['search']?.value ?? '';
      this.productService.getSearchProducts(text);
    }
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }
}
