import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Product } from '../models/product';
import { ShopService } from '../shop.service';

@Component({
  selector: 'app-content',
  templateUrl: './content.component.html',
  styleUrls: ['./content.component.scss'],
})
export class ContentComponent implements OnInit {
  private subcategoryProducts: Product[] = [];
  private subcategoryName!: string;

  constructor(
    private route: ActivatedRoute,
    private shopService: ShopService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      const subcategoryId = params.get('id') as string;
      console.log(subcategoryId);
      if (subcategoryId) {
        this.getProductsBySubcategoryId(subcategoryId);
      }
      this.subcategoryName = params.get('subcategoryName') as string;
    });
  }

  private getProductsBySubcategoryId(subcategoryId: string) {
    this.shopService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((products) => {
        this.subcategoryProducts = products;
        console.log(this.subcategoryProducts);
      });
  }

  public get getProducts(): Product[] {
    return this.subcategoryProducts;
  }

  public get getSubcategoryName() {
    return this.subcategoryName;
  }
}
