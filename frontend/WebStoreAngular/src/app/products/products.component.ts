import { Component, Input, OnInit } from '@angular/core';
import { Product } from '../models/product';
import { ShopService } from '../services/shop.service';

@Component({
  selector: 'app-products',
  templateUrl: './products.component.html',
  styleUrls: ['./products.component.scss'],
})
export class ProductsComponent implements OnInit {
  @Input()
  products: Product[] = [];

  constructor(private shopService: ShopService) {}

  ngOnInit(): void {}

  public increaseNumberOfProduct(product: Product): void {
    if (product.amountOfProduct < 100) {
      product.amountOfProduct++;
    }
  }

  public decreaseNumberOfProduct(product: Product): void {
    if (product.amountOfProduct > 1) {
      product.amountOfProduct--;
    }
  }

  public getPrice(product: Product): string {
    return (parseFloat(product.price) * product.amountOfProduct).toFixed(2);
  }
}
