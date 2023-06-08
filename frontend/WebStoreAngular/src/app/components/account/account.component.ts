import { Component, OnInit } from '@angular/core';
import { AccountResponse } from 'src/app/models/account-response';
import { AccountService } from 'src/app/services/account.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  private accountId!: string;
  private account!: AccountResponse;

  constructor(private accountService: AccountService) {
    this.accountId = sessionStorage.getItem('id') || '';
  }

  ngOnInit(): void {
    this.accountService.getAccount(this.accountId).subscribe((account) => {
      this.account = account;
    });
  }

  public get user() {
    return this.account;
  }

  public getUserImageUrl() {
    return this.account.imageUrl === null
      ? 'https://ik.imagekit.io/glowacki/a23SANX.png?updatedAt=1686001892311'
      : this.account.imageUrl;
  }
}
