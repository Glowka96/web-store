import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { AccountAddress } from 'src/app/models/accounts/account-address';
import { AccountRequest } from 'src/app/models/accounts/account-request';
import { AccountResponse } from 'src/app/models/accounts/account-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAccount(): Observable<AccountResponse> {
    return this.http.get<AccountResponse>(`${this.apiServerUrl}/accounts`);
  }

  public getAccountAddress(): Observable<AccountAddress> {
    return this.http.get<AccountAddress>(
      `${this.apiServerUrl}/accounts/addresses`
    );
  }

  public updateAccount(request: AccountRequest): Observable<AccountResponse> {
    return this.http.put<AccountResponse>(
      `${this.apiServerUrl}/accounts`,
      request
    );
  }

  public addAddress(request: AccountAddress): Observable<AccountAddress> {
    return this.http.post<AccountAddress>(
      `${this.apiServerUrl}/accounts/address`,
      request
    );
  }

  public updateAddress(request: AccountAddress): Observable<AccountAddress> {
    return this.http.put<AccountAddress>(
      `${this.apiServerUrl}/accounts/addresses`,
      request
    );
  }
}
