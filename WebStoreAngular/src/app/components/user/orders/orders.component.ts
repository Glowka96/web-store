import { DatePipe } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { take } from 'rxjs';
import { OrderResponse } from 'src/app/models/orders/order-response';
import { AddressFormBuilderService } from 'src/app/services/forms/users/address-form-builder.service';
import { OrdersService } from 'src/app/services/olders/orders.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
})
export class OrdersComponent implements OnInit {
  private _orders!: OrderResponse[];

  public deliveryAddressForm!: FormGroup;

  constructor(
    private ordersService: OrdersService,
    private addressFormService: AddressFormBuilderService
  ) {
    this.ordersService
      .getAllAccountOrders()
      .pipe(take(1))
      .subscribe((orders) => {
        this._orders = orders;
      });
  }

  ngOnInit(): void {
    this.deliveryAddressForm =
      this.addressFormService.createAccountAddressFormGroup();
  }

  public get orders() {
    return this._orders;
  }
}
