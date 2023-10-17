import { Component, OnInit } from '@angular/core';
import { AccountResponse } from 'src/app/models/account-response';
import { AccountService } from 'src/app/services/accounts/account.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  private account!: AccountResponse;

  constructor(private accountService: AccountService) {}

  ngOnInit(): void {
    this.accountService.getAccount().subscribe((account) => {
      this.account = account;
    });
  }

  public get user() {
    return this.account;
  }

  public getUserImageUrl() {
    return this.account
      ? this.account.imageUrl
      : 'https://ik.imagekit.io/glowacki/a23SANX.png?updatedAt=1686001892311';
  }

  public get titleButton() {
    return this.account?.address ? 'Update Address' : 'Add Address';
  }
}
