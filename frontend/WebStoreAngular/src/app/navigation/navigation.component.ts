import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Category } from '../models/category';
import { FormLoginService } from '../services/form-login.service';
import { ShopService } from '../services/shop.service';
import { AuthenticationService } from '../services/authentication.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private loggedIn: boolean = false;

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService,
    private formLoginService: FormLoginService,
    private authService: AuthenticationService
  ) {
    shopService.categories.subscribe((categories) => {
      this.categories = categories;
    });
    authService.isLoggedIn().subscribe((value) => {
      this.loggedIn = value;
    });
  }

  public get getCategories(): Category[] {
    return this.categories;
  }

  public isLoggedIn(): boolean {
    return this.loggedIn;
  }

  public logout(): void {
    this.authService.logout();
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }

  ngOnInit() {}
}
