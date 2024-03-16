import { Component, OnInit } from '@angular/core';
import { take } from 'rxjs';
import { AccountResponse } from 'src/app/models/accounts/account-response';
import { OrderResponse } from 'src/app/models/orders/order-response';
import { AccountService } from 'src/app/services/accounts/account.service';
import { OrderService } from 'src/app/services/olders/order.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  private _account!: AccountResponse;
  private _orders: OrderResponse[] = [];

  constructor(
    private accountService: AccountService,
    private orderService: OrderService
  ) {}

  ngOnInit(): void {
    this.accountService
      .getAccount()
      .pipe(take(1))
      .subscribe((account) => {
        this._account = account;
      });
    this.orderService
      .getLastFiveAccountOrders()
      .pipe(take(1))
      .subscribe((orders) => (this._orders = orders));
  }

  public get account() {
    return this._account;
  }

  public get orders() {
    return this._orders;
  }

  public getUserImageUrl() {
    return this._account
      ? this._account.imageUrl
      : 'https://ik.imagekit.io/glowacki/a23SANX.png?updatedAt=1686001892311';
  }

  public get titleButton() {
    return this._account?.address ? 'Update Address' : 'Add Address';
  }
}
