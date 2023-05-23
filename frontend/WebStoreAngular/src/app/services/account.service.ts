import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Observable, tap } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountAddress } from '../models/account-address';
import { AccountResponse } from '../models/account-response';
import { AccountRequest } from '../models/account-request';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAccount(accountId: string): Observable<AccountResponse> {
    return this.http.get<AccountResponse>(
      `${this.apiServerUrl}/accounts/${accountId}`
    );
  }

  public getAccountAddress(accountId: string): Observable<AccountAddress> {
    return this.http.get<AccountAddress>(
      `${this.apiServerUrl}/accounts/${accountId}/address`
    );
  }

  public updateAccount(
    accountId: string,
    request: AccountRequest
  ): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(
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
