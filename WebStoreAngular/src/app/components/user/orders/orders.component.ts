import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { take } from 'rxjs';
import { OrderRequest } from 'src/app/models/orders/order-request';
import { OrderResponse } from 'src/app/models/orders/order-response';
import { AddressFormBuilderService } from 'src/app/services/forms/users/address-form-builder.service';
import { OrdersService } from 'src/app/services/olders/orders.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
})
export class OrdersComponent implements OnInit {
  private orders!: OrderResponse[];
  private errorMessage!: string;
  private selectedErrorId!: string;
  private selectedUpdatedId!: string;

  public deliveryAddressForm!: FormGroup;

  constructor(
    private ordersService: OrdersService,
    private addressFormService: AddressFormBuilderService
  ) {
    this.ordersService
      .getAllAccountOrders()
      .pipe(take(1))
      .subscribe((orders) => {
        this.orders = orders;
      });
  }

  ngOnInit(): void {
    this.deliveryAddressForm =
      this.addressFormService.createAccountAddressFormGroup();
  }

  public get listOrder() {
    return this.orders;
  }

  public isViewErorr(orderId: string) {
    return this.selectedErrorId === orderId;
  }

  public isUpdated(orderId: string) {
    return this.selectedUpdatedId === orderId;
  }

  public get errorMsg() {
    return this.errorMessage;
  }
}
