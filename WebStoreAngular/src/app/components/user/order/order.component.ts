import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { take } from 'rxjs/internal/operators/take';
import { OrderResponse } from 'src/app/models/orders/order-response';
import { OrderService } from 'src/app/services/olders/order.service';

@Component({
  selector: 'app-order',
  templateUrl: './order.component.html',
  styleUrls: ['./order.component.scss'],
})
export class OrderComponent implements OnInit {
  private _order!: OrderResponse;

  constructor(
    private route: ActivatedRoute,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        const orderId = params.get('id') as string;
        this.orderService
          .getOrderById(orderId)
          .pipe(take(1))
          .subscribe((order) => {
            this._order = order;
          });
      }
    });
  }

  public get order() {
    return this._order;
  }
}
