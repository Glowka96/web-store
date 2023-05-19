import { Component, OnInit } from '@angular/core';
import { Account } from 'src/app/models/account';
import { AccountService } from 'src/app/services/account.service';
import { AuthenticationService } from 'src/app/services/authentication.service';

@Component({
  selector: 'app-account',
  templateUrl: './account.component.html',
  styleUrls: ['./account.component.scss'],
})
export class AccountComponent implements OnInit {
  private accountId!: string;
  private account!: Account;

  constructor(
    private authService: AuthenticationService,
    private accountService: AccountService
  ) {
    this.authService.loggedId$().subscribe((id) => {
      this.accountId = id;
    });
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
    if (this.account) {
      return this.account.imageUrl;
    } else {
      return 'https://i.imgur.com/a23SANX.png';
    }
  }
}
