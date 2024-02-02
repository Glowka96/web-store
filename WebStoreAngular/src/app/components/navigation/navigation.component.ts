import { Component, HostListener, OnInit } from '@angular/core';

import {
  ControlContainer,
  FormControl,
  FormGroup,
  FormGroupDirective,
  Validators,
} from '@angular/forms';
import { CategoryResponse } from 'src/app/models/category-response';
import { CategoryService } from 'src/app/services/products/category.service';
import { BreakpointObserver, Breakpoints } from '@angular/cdk/layout';
import { AuthenticationService } from 'src/app/services/accounts/authentication.service';
import { FormLoginService } from 'src/app/services/accounts/form-login.service';
import { SearchProductsService } from 'src/app/services/page-products/search-products.service';
import { ActivatedRoute, Router } from '@angular/router';

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
    private formLoginService: FormLoginService,
    private authService: AuthenticationService,
    private categoryService: CategoryService,
    private breakpointObserver: BreakpointObserver,
    private router: Router,
    private route: ActivatedRoute
  ) {
    categoryService.categories$.subscribe((categories) => {
      this.categories = categories;
    });
    authService.isAuthenticated$.subscribe((isLogIn) => {
      this.isLogIn = isLogIn;
    });
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
      this.router.navigate(['products/search'], {
        relativeTo: this.route,
        queryParams: {
          query: this.searchForm.controls['search'].value,
          page: 0,
          size: 12,
          sort: 'id',
          direction: 'asc',
        },
        queryParamsHandling: 'merge',
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
