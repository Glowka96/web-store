import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Subscription, take } from 'rxjs';
import { Shipment } from 'src/app/models/orders/shipment';
import { FormLoginService } from 'src/app/services/accounts/form-login.service';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss'],
})
export class BasketComponent implements OnInit {
  private _basket: Shipment[] = [];
  private _selectedId!: number;
  private _isLogIn = false;
  private _isBuyBtnClicked = false;
  protected _routeSubscription!: Subscription;

  public changeForm = new FormGroup({
    quantity: new FormControl('', {
      validators: [Validators.min(1), Validators.pattern('\\d+')],
      updateOn: 'change',
    }),
  });

  constructor(
    private shopService: ShopService,
    private formLoginService: FormLoginService,
    private router: Router
  ) {
    this._routeSubscription = this.shopService.basket$
      .pipe(take(1))
      .subscribe((shipments) => {
        this._basket = shipments;
      });
  }

  ngOnInit(): void {
    const isLogIn = sessionStorage.getItem('isLoggedIn');
    isLogIn === 'true' ? (this._isLogIn = true) : (this._isLogIn = false);
  }

  ngOnDestroy(): void {
    if (this._routeSubscription) {
      this._routeSubscription.unsubscribe();
    }
  }

  public get basket() {
    return this._basket;
  }

  public get isBasketEmpty() {
    return this._basket?.length > 0;
  }

  public deleteProductFromBasket(productId: number) {
    const index = this.getIndexBasket(productId);
    this._basket.splice(index, 1);
  }

  public isUpdate(shipmentId: number) {
    return this._selectedId === shipmentId;
  }

  public change(shipmentId: number) {
    if (!this._selectedId) {
      this._selectedId = shipmentId;
    } else {
      this._selectedId = 0;
    }
  }

  public onSumbitChange(productId: number) {
    if (this.changeForm.valid) {
      const index = this.getIndexBasket(productId);
      const quantity = this.changeForm.controls['quantity']?.value;
      if (this._basket[index].product.quantity > Number(quantity)) {
        this._basket[index].quantity = Number(quantity);
        this.change(0);
        this.changeForm.controls['quantity'].reset();
      }
    }
  }

  public onSumbitBuy() {
    this._isBuyBtnClicked = !this._isBuyBtnClicked;
    console.log(this.isLoggedIn);
    if (this.isLoggedIn) {
      this.router.navigate(['/basket/purchase'], {});
    }
  }

  public onLoginForm() {
    this.formLoginService.changeStatusFormLogin();
  }

  private getIndexBasket(productId: number) {
    return this._basket.findIndex((shipment) => {
      return shipment.product.id === productId;
    });
  }

  public getShipmentPrice(productId: number) {
    const index = this.getIndexBasket(productId);
    const product = this._basket[index].product;
    const quantity = this._basket[index].quantity;
    return product.promotionPrice
      ? (quantity * product.promotionPrice).toFixed(2)
      : (quantity * product.price).toFixed(2);
  }

  public get isLoggedIn() {
    return this._isLogIn;
  }

  public get isBuyButtonClicked() {
    return this._isBuyBtnClicked;
  }
}
