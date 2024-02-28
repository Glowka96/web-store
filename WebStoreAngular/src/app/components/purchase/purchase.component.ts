import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { Subscription } from 'rxjs/internal/Subscription';
import { AccountAddress } from 'src/app/models/account-address';
import { DeliveryTypeResponse } from 'src/app/models/orders/delivery-type-response';
import { OrderRequest } from 'src/app/models/orders/order-request';
import { Shipment } from 'src/app/models/orders/shipment';
import { AccountService } from 'src/app/services/accounts/account.service';
import { AddressFormBuilderService } from 'src/app/services/forms/users/address-form-builder.service';
import { DeliveryTypeFormBuilderService } from 'src/app/services/forms/users/delivery-type-form-builder.service';
import { DeliveryTypeService } from 'src/app/services/olders/delivery-type.service';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.scss'],
})
export class PurchaseComponent implements OnInit {
  private _subscriptions: Subscription[] = [];
  private _accountAddress!: AccountAddress;
  private _foundAddress!: boolean;
  private _basket!: Shipment[];
  private _shipmentsPrice = 0;
  private _submitPurchase = false;
  private _message!: string;
  private _deliveryTypes!: DeliveryTypeResponse[];

  public deliveryAddressForm!: FormGroup;
  public deliveryTypeForm!: FormGroup;

  constructor(
    private accountService: AccountService,
    private shopService: ShopService,
    private addressFormService: AddressFormBuilderService,
    private deliveryTypeService: DeliveryTypeService,
    private deliveryTypeFormService: DeliveryTypeFormBuilderService,
    private router: Router
  ) {
    const sub1 = this.accountService.getAccountAddress().subscribe({
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
    const sub2 = this.shopService.basket$.subscribe((shipments) => {
      this._basket = shipments;
      shipments.forEach((s) => {
        let shipmentPrice = '';
        s.product.promotionPrice
          ? (shipmentPrice = (s.product.promotionPrice * s.quantity).toFixed(2))
          : (shipmentPrice = (s.product.price * s.quantity).toFixed(2));
        this._shipmentsPrice += Number(shipmentPrice);
      });
    });
    const sub3 = this.deliveryTypeService
      .getAllDeliveryType()
      .subscribe((types) => {
        this._deliveryTypes = types;
        console.log(this._deliveryTypes);
      });
    this._subscriptions.push(sub1, sub2, sub3);
  }

  ngOnInit(): void {
    this.deliveryAddressForm =
      this.addressFormService.createAccountAddressFormGroup();
    this.deliveryTypeForm =
      this.deliveryTypeFormService.createDeliveryFormGroup();
  }

  ngOnDestroy(): void {
    this._subscriptions.forEach((s) => s.unsubscribe);
  }

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
    if (this.deliveryAddressForm.valid && this.deliveryTypeForm.valid) {
      const city = this.deliveryAddressForm.controls['city']?.value;
      const postcode = this.deliveryAddressForm.controls['postcode']?.value;
      const street = this.deliveryAddressForm.controls['street']?.value;
      const deliveryTypeId = this.deliveryTypeForm.controls['choice']?.value;
      const request: OrderRequest = {
        shipments: this._basket.map((shipment) => {
          return {
            productId: shipment.product.id,
            quantity: shipment.quantity,
          };
        }),
        delivery: {
          deliveryAddress: city + ', ' + postcode + ', ' + street,
          deliveryTypeId: deliveryTypeId,
        },
      };
      this.shopService
        .purchase(request)
        .pipe(take(1))
        .subscribe({
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

  public get price(): string {
    if (this.deliveryTypeForm.valid) {
      const deliveryTypeId = this.deliveryTypeForm.get('choice')?.value;
      const delivery = this.deliveryTypes.find(
        (delivery) => delivery.id === deliveryTypeId
      );
      if (delivery) {
        return (this._shipmentsPrice + delivery.price).toFixed(2);
      }
    }
    return this._shipmentsPrice.toFixed(2);
  }

  public get isSubmitPurchase() {
    return this._submitPurchase;
  }

  public get sumbitMessage() {
    return this._message;
  }

  public get deliveryTypes() {
    return this._deliveryTypes;
  }
}
