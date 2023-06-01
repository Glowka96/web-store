import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { AccountAddress } from 'src/app/models/account-address';
import { OrderRequest } from 'src/app/models/order-request';
import { ShipmentRequest } from 'src/app/models/shipment-request';
import { AccountService } from 'src/app/services/account.service';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-purchase',
  templateUrl: './purchase.component.html',
  styleUrls: ['./purchase.component.scss'],
})
export class PurchaseComponent implements OnInit {
  private accountId!: string;
  private accountAddress!: AccountAddress;
  private foundAddress!: boolean;
  private basket!: ShipmentRequest[];
  private shipmentsPrice: number = 0;
  private submitPurchase: boolean = false;
  private message!: string;
  private postcodePattern = /^\d{2}-\d{3}$/;
  private addressPattern =
    /^(ul(.)\s)?[A-Z]?[a-z]*\s[0-9]{1,3}(\/[0-9]{1,3})|(\sm\.?\s[0-9]{1,3})[a-z]?$/;

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
        Validators.pattern(this.postcodePattern),
      ],
      updateOn: 'change',
    }),
    street: new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this.addressPattern),
      ],
      updateOn: 'change',
    }),
  });

  constructor(
    private accountService: AccountService,
    private shopService: ShopService,
    private router: Router
  ) {
    this.accountId = sessionStorage.getItem('id') || '';
    this.accountService.getAccountAddress(this.accountId).subscribe({
      next: (response) => {
        this.accountAddress = response;
        this.foundAddress = true;
        console.log(this.accountAddress);
      },
      error: (error) => {
        if (error.status === 404) {
          this.foundAddress = false;
        }
      },
    });
    this.shopService.basket$.subscribe((shipments) => {
      this.basket = shipments;
      shipments.forEach((shipment) =>
        (this.shipmentsPrice += Number(shipment.price)).toFixed(2)
      );
    });
  }

  ngOnInit(): void {}

  public autocomplete() {
    this.deliveryAddressForm.controls['city'].setValue(
      this.accountAddress.city
    );
    this.deliveryAddressForm.controls['postcode'].setValue(
      this.accountAddress.postcode
    );
    this.deliveryAddressForm.controls['street'].setValue(
      this.accountAddress.street
    );
  }

  public purchase() {
    if (this.deliveryAddressForm.valid) {
      let city = this.deliveryAddressForm.controls['city']?.value ?? '';
      let postcode = this.deliveryAddressForm.controls['postcode']?.value ?? '';
      let street = this.deliveryAddressForm.controls['street']?.value ?? '';
      let request: OrderRequest = {
        shipments: this.basket,
        deliveryAddress: city + ', ' + postcode + ', ' + street,
      };
      this.shopService.purchase(this.accountId, request).subscribe({
        next: () => {
          this.shopService.basket$.next([]);
          this.router.navigate(['/accounts']);
        },
        error: (error) => {
          if (error.status === 404) {
            this.submitPurchase = true;
            this.message = error.error.errors;
          }
        },
      });
    }
  }

  public get isFoundAddress() {
    return this.foundAddress;
  }

  public get quantity() {
    return this.basket.length;
  }

  public get price() {
    return this.shipmentsPrice;
  }

  public get isSubmitPurchase() {
    return this.submitPurchase;
  }

  public get sumbitMessage() {
    return this.message;
  }
}
