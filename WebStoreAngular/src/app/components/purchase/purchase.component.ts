import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountAddress } from 'src/app/models/account-address';
import { OrderRequest } from 'src/app/models/order-request';
import { Shipment } from 'src/app/models/shipment';
import { ShipmentRequest } from 'src/app/models/shipment-request';
import { AccountService } from 'src/app/services/accounts/account.service';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.scss'],
})
export class PurchaseComponent implements OnInit {
  private _accountAddress!: AccountAddress;
  private _foundAddress!: boolean;
  private _basket!: Shipment[];
  private _shipmentsPrice = 0;
  private _submitPurchase = false;
  private _message!: string;
  private _postcodePattern = /^\d{2}-\d{3}$/;
  private _addressPattern =
    /^((ul\.?\s)?([A-Z]?[a-z]{3,20}(-[A-Z]?[a-z]{3,20})?)\s(\d{1,3})[a-z]?((\/|\sm(.)?\s)\d{1,3})?)$/;
  public deliveryAddressForm = new FormGroup({
    city: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(2),
        Validators.maxLength(32),
        Validators.pattern('[a-zA-Z]*'),
      ],
      updateOn: 'change',
    }),
    postcode: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this._postcodePattern),
      ],
      updateOn: 'change',
    }),
    street: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this._addressPattern),
      ],
      updateOn: 'change',
    }),
  });

  constructor(
    private accountService: AccountService,
    private shopService: ShopService,
    private router: Router
  ) {
    this.accountService.getAccountAddress().subscribe({
      next: (response) => {
        this._accountAddress = response;
        this._foundAddress = true;
      },
      error: (error) => {
        if (error.status === 404) {
          this._foundAddress = false;
        }
      },
    });
    this.shopService.basket$.subscribe((shipments) => {
      this._basket = shipments;
      shipments.forEach((s) => {
        let shipmentPrice = '';
        s.product.promotionPrice
          ? (shipmentPrice = (s.product.promotionPrice * s.quantity).toFixed(2))
          : (shipmentPrice = (s.product.price * s.quantity).toFixed(2));
        this._shipmentsPrice += Number(shipmentPrice);
      });
    });
  }

  ngOnInit(): void {}

  public autocomplete() {
    this.deliveryAddressForm.controls['city'].setValue(
      this._accountAddress.city
    );
    this.deliveryAddressForm.controls['postcode'].setValue(
      this._accountAddress.postcode
    );
    this.deliveryAddressForm.controls['street'].setValue(
      this._accountAddress.street
    );
  }

  public purchase() {
    if (this.deliveryAddressForm.valid) {
      const city = this.deliveryAddressForm.controls['city']?.value ?? '';
      const postcode =
        this.deliveryAddressForm.controls['postcode']?.value ?? '';
      const street = this.deliveryAddressForm.controls['street']?.value ?? '';
      const request: OrderRequest = {
        shipments: this._basket.map((shipment) => {
          return {
            productId: shipment.product.id,
            quantity: shipment.quantity,
          };
        }),
        deliveryAddress: city + ', ' + postcode + ', ' + street,
      };
      this.shopService.purchase(request).subscribe({
        next: () => {
          this.shopService.basket$.next([]);
          this.router.navigate(['/accounts/orders']);
        },
        error: (error) => {
          this._submitPurchase = true;
          this._message = error.error.errors;
        },
      });
    }
  }

  public get isFoundAddress() {
    return this._foundAddress;
  }

  public get quantity() {
    return this._basket.length;
  }

  public get price() {
    return this._shipmentsPrice;
  }

  public get isSubmitPurchase() {
    return this._submitPurchase;
  }

  public get sumbitMessage() {
    return this._message;
  }
}
