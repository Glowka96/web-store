import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Category } from '../models/category';
import { FormLoginService } from '../services/form-login.service';
import { ShopService } from '../services/shop.service';
import { AuthenticationService } from '../services/authentication.service';
import { FormControl, FormGroup, Validators } from '@angular/forms';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private loggedIn: boolean = false;

  public searchForm = new FormGroup({
    search: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  constructor(
    private shopService: ShopService,
    private formLoginService: FormLoginService,
    private authService: AuthenticationService
  ) {
    shopService.categories$.subscribe((categories) => {
      this.categories = categories;
    });
    authService.isLoggedIn().subscribe((value) => {
      this.loggedIn = value;
    });
  }

  ngOnInit() {}

  public get getCategories(): Category[] {
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
      let text = this.searchForm.get('search')?.value ?? '';
      this.shopService.getSearchProducts(text);
    }
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }
}
