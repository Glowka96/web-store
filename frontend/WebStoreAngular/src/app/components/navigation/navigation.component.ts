import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Category } from 'src/app/models/category';
import { ShopService } from 'src/app/services/shop.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { FormLoginService } from 'src/app/services/form-login.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private loggedIn: boolean = false;
  private loggedRole!: string;

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
      console.log(value);
      this.loggedIn = value;
    });
    authService.isLoggedRole().subscribe((value) => {
      console.log('role: ' + value);
      this.loggedRole = value;
    });
  }

  ngOnInit() {}

  public get getCategories(): Category[] {
    return this.categories;
  }

  public isLoggedIn(): boolean {
    return this.loggedIn;
  }

  public isAdmin(): boolean {
    const is = this.loggedRole.includes('ROLE_USER');
    console.log(is);
    return this.loggedIn && this.loggedRole.includes('ROLE_USER');
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
