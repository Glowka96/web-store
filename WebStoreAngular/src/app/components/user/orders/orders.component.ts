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

  public updateOrder(orderId: string) {
    if (!this.selectedUpdatedId) {
      this.deliveryAddressForm.reset();
      this.selectedUpdatedId = orderId;
    } else {
      this.selectedUpdatedId = '';
    }
  }

  public deleteOrder(orderId: string) {
    this.ordersService
      .deleteOrder(orderId)
      .pipe(take(1))
      .subscribe({
        next: () => {
          window.location.reload();
        },
        error: (error) => {
          this.selectedErrorId = orderId;
          const errorMessage = error.error.errors;
          this.errorMessage = errorMessage;
        },
      });
  }

  public onSumbitUpdate(orderId: string) {
    if (this.deliveryAddressForm.valid) {
      const findOrder = this.orders.find((order) => {
        return order.id === orderId;
      });
      if (findOrder) {
        const city = this.deliveryAddressForm.controls['city']?.value ?? '';
        const postcode =
          this.deliveryAddressForm.controls['postcode']?.value ?? '';
        const street = this.deliveryAddressForm.controls['street']?.value ?? '';
        const request: OrderRequest = {
          deliveryAddress: city + ', ' + postcode + ', ' + street,
          shipments: [],
        };
        this.ordersService.updateOrder(findOrder.id, request).subscribe({
          next: () => {
            window.location.reload();
          },
          error: (error) => {
            this.selectedErrorId = orderId;
            const errorMessage = error.error.errors;
            this.errorMessage = errorMessage;
          },
        });
      }
    }
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
