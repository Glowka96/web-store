import { Component, OnInit } from '@angular/core';
import { OrderResponse } from 'src/app/models/order-response';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { OrdersService } from 'src/app/services/orders.service';

@Component({
  selector: 'app-orders',
  templateUrl: './orders.component.html',
  styleUrls: ['./orders.component.scss'],
})
export class OrdersComponent implements OnInit {
  private accountId!: string;
  private orders!: OrderResponse[];
  private errorMessage!: string;
  private selectedId!: string;

  constructor(private ordersService: OrdersService) {
    this.accountId = sessionStorage.getItem('id') || '';
    this.ordersService
      .getAllAccountOrders(this.accountId)
      .subscribe((orders) => {
        this.orders = orders;
        console.log(orders);
      });
  }

  ngOnInit(): void {}

  public deleteOrder(orderId: string) {
    this.ordersService.deleteOrder(this.accountId, orderId).subscribe({
      next: () => {
        window.location.reload();
      },
      error: (error) => {
        this.selectedId = orderId;
        let errorMessage = error.error.errors;
        this.errorMessage = errorMessage;
      },
    });
  }

  public get listOrder() {
    return this.orders;
  }

  public isViewErorr(orderId: string) {
    return this.selectedId === orderId;
  }

  public get errorMsg() {
    return this.errorMessage;
  }
}
