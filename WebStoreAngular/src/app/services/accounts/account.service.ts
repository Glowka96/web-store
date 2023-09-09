import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountAddress } from 'src/app/models/account-address';
import { AccountRequest } from 'src/app/models/account-request';
import { AccountResponse } from 'src/app/models/account-response';
import { environment } from 'src/environments/environment';

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

  public addAddress(
    accountId: string,
    request: AccountAddress
  ): Observable<AccountAddress> {
    return this.http.post<AccountAddress>(
      `${this.apiServerUrl}/accounts/${accountId}/address`,
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
