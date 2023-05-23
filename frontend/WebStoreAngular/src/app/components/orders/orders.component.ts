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

  constructor(
    private authService: AuthenticationService,
    private ordersService: OrdersService
  ) {
    this.authService.loggedId$().subscribe((id) => {
      this.accountId = id;
    });
    this.ordersService
      .getAllAccountOrders(this.accountId)
      .subscribe((orders) => {
        this.orders = orders;
        console.log(orders);
      });
  }

  ngOnInit(): void {}

  public get listOrder() {
    return this.orders;
  }
}
