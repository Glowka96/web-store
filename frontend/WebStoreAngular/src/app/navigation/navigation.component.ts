import { Component, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { CategoryService } from '../category.service';
import { Category } from '../model/category';

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.scss']
})
export class NavigationComponent implements OnInit {
  private categories: Category[] = [];
  private sub: Subscription;

  constructor(private categoryService: CategoryService) {
    this.sub = categoryService.categories.subscribe( (categories) => {
      this.categories = categories;
      console.log(this.categories);
    })
   }

  public get getCategories(): Category[]{
    return this.categories;
  }


  ngOnInit(): void {
  }

}
