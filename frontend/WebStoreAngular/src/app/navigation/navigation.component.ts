import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs';
import { Category } from '../models/category';
import { FormLoginService } from '../services/form-login.service';
import { ShopService } from '../services/shop.service';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss'],
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private sub: Subscription;
  private isLoggedIn = false;
  private showAdminBoard = false;
  private showUserBoard = false;

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService,
    private formLoginService: FormLoginService
  ) {
    this.sub = shopService.categories.subscribe((categories) => {
      this.categories = categories;
    });
  }

  public get getCategories(): Category[] {
    return this.categories;
  }

  public changeStatusLogginForm() {
    this.formLoginService.changeStatusFormLogin();
  }

  ngOnInit() {}
}
