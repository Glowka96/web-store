import { Component, HostListener, OnInit } from '@angular/core';

import {
  ControlContainer,
  FormControl,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import { CategoryResponse } from 'src/app/models/category-response';
import { CategoryService } from 'src/app/services/category.service';
import { ProductService } from 'src/app/services/product.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthenticationService } from 'src/app/services/accounts/authentication.service';
import { FormLoginService } from 'src/app/services/accounts/form-login.service';

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
  private isLogIn = false;
  private isMobile = false;

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
    private categoryService: CategoryService,
    private breakpointObserver: BreakpointObserver
  ) {
    categoryService.categories$.subscribe((categories) => {
      this.categories = categories;
    });
    const isLogIn = sessionStorage.getItem('isLoggedIn');
    isLogIn === 'true' ? (this.isLogIn = true) : (this.isLogIn = false);
  }

  ngOnInit() {
    this.breakpointObserver
      .observe([
        Breakpoints.XSmall,
        Breakpoints.Small,
        Breakpoints.Medium,
        Breakpoints.Tablet,
        Breakpoints.TabletLandscape,
      ])
      .subscribe((result) => {
        this.isMobile = result.matches;
      });
  }

  public get getCategories(): CategoryResponse[] {
    return this.categories;
  }

  public isLoggedIn(): boolean {
    return this.isLogIn;
  }

  public logout(): void {
    this.authService.logout();
  }

  public onSearch(): void {
    if (this.searchForm.valid) {
      const text = this.searchForm.controls['search']?.value ?? '';
      this.productService.getSearchProducts(text);
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    }
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }

  public get isModbileWidth() {
    return this.isMobile;
  }
}
