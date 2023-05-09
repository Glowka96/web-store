import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Shipment } from 'src/app/models/shipment';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss'],
})
export class BasketComponent implements OnInit {
  private basket: Shipment[] = [];
  private update: boolean = false;

  public changeForm = new FormGroup({
    quantity: new FormControl('', {
      validators: [Validators.min(1), Validators.max(100)],
      updateOn: 'change',
    }),
  });

  constructor(private shopService: ShopService) {
    this.shopService.basket$.subscribe((shipments) => {
      this.basket = shipments;
      console.log('basket: ' + this.basket);
    });
  }

  ngOnInit(): void {}

  public get shipments() {
    return this.basket;
  }

  public get isBasketEmpty() {
    return this.basket?.length > 0;
  }

  public deleteProductFromBasket(productId: string) {
    let index = this.getIndexBasket(productId);
    this.basket.splice(index, 1);
  }

  public isUpdate() {
    return this.update;
  }

  public change() {
    this.update = !this.update;
  }

  public onSumbitChange(productId: string) {
    if (this.changeForm.valid) {
      let index = this.getIndexBasket(productId);
      let quantity = this.changeForm.controls['quantity']?.value;

      this.basket[index].quantity = Number(quantity);
      this.basket[index].price =
        Number(quantity) * Number(this.basket[index].product.price);
      this.change();
    }
  }

  private getIndexBasket(productId: string) {
    return this.basket.findIndex((shipment) => {
      return shipment.product.id === productId;
    });
  }
}
