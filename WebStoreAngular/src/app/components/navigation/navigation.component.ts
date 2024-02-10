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
import { ActivatedRoute, Router } from '@angular/router';
import { ShopService } from 'src/app/services/olders/shop.service';
import { Shipment } from 'src/app/models/shipment';
import { Subscription } from 'rxjs';

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
  private _basket: Shipment[] = [];
  private _subscriptions: Subscription[] = [];

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
    private shopService: ShopService,
    private breakpointObserver: BreakpointObserver,
    private router: Router,
    private route: ActivatedRoute
  ) {
    const sub1 = categoryService.categories$.subscribe((categories) => {
      this.categories = categories;
    });
    const sub2 = authService.isAuthenticated$.subscribe((isLogIn) => {
      this.isLogIn = isLogIn;
    });
    const sub3 = shopService.basket$.subscribe((shipments) => {
      this._basket = shipments;
    });
    this._subscriptions.push(sub1, sub2, sub3);
  }

  ngOnInit() {
    const sub = this.breakpointObserver
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
    this._subscriptions.push(sub);
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach((s) => s.unsubscribe);
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
          page: 1,
          size: 12,
          sort: 'id',
          direction: 'asc',
        },
        queryParamsHandling: 'merge',
      });
      this.searchForm.reset();
    }
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }

  public get isModbileWidth() {
    return this.isMobile;
  }

  public get basket() {
    return this._basket;
  }
}
