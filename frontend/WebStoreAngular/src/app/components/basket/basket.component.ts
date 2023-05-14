import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Shipment } from 'src/app/models/shipment';
import { AccountService } from 'src/app/services/account.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { FormLoginService } from 'src/app/services/form-login.service';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss'],
})
export class BasketComponent implements OnInit {
  private basket: Shipment[] = [];
  private cart: { [shipment: string]: number } = {};
  private selectedId!: string;
  private loggedIn: boolean = false;
  private buyBtnClicked = false;

  public changeForm = new FormGroup({
    quantity: new FormControl('', {
      validators: [Validators.min(1), Validators.max(100)],
      updateOn: 'change',
    }),
  });

  constructor(
    private shopService: ShopService,
    private authService: AuthenticationService,
    private formLoginService: FormLoginService,
    private router: Router
  ) {
    this.shopService.basket$.subscribe((shipments) => {
      this.basket = shipments;
    });
  }

  ngOnInit(): void {
    this.authService.loggedIn$().subscribe((isLogged) => {
      this.loggedIn = isLogged;
    });
  }

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

  public isUpdate(shipmentId: string) {
    return this.selectedId === shipmentId;
  }

  public change(shipmentId: string) {
    this.selectedId = shipmentId;
  }

  public onSumbitChange(productId: string) {
    if (this.changeForm.valid) {
      let index = this.getIndexBasket(productId);
      let quantity = this.changeForm.controls['quantity']?.value;

      this.basket[index].quantity = Number(quantity);
      this.basket[index].price = (
        Number(quantity) * Number(this.basket[index].product.price)
      ).toFixed(2);
      this.change('');
      this.changeForm.controls['quantity'].reset();
    }
  }

  public onSumbitBuy() {
    this.buyBtnClicked = !this.buyBtnClicked;
    if (this.isLoggedIn) {
      this.router.navigate(['/basket/purchase'], {});
    }
  }

  public onRegister() {
    this.formLoginService.changeStatusFormLogin();
  }

  private getIndexBasket(productId: string) {
    return this.basket.findIndex((shipment) => {
      return shipment.product.id === productId;
    });
  }

  public get isLoggedIn() {
    return this.loggedIn;
  }

  public get isBuyBtnClicked() {
    return this.buyBtnClicked;
  }
}
