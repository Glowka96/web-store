import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountAddress } from '../models/account-address';
import { Account } from '../models/account';
import { AccountRequest } from '../models/account-request';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient, private router: Router) {}

  public getAccount(accountId: string): Observable<Account> {
    return this.http.get<Account>(`${this.apiServerUrl}/accounts/${accountId}`);
  }

  public getAccountAddress(accountId: string): Observable<AccountAddress> {
    return this.http.get<AccountAddress>(
      `${this.apiServerUrl}/accounts/${accountId}/address`
    );
  }

  public updateAccount(
    accountId: string,
    request: AccountRequest
  ): Observable<Account> {
    return this.http.put<Account>(
      `${this.apiServerUrl}/accounts/${accountId}`,
      request
    );
  }

  public updateAddress(
    accountId: string,
    request: AccountAddress
  ): Observable<AccountAddress> {
    return this.http.put<AccountAddress>(
      `${this.apiServerUrl}/accounts/${accountId}/address`,
      request
    );
  }
}
