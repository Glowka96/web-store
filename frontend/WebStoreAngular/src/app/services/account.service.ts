import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { BehaviorSubject, Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AccountAddress } from '../models/account-address';

@Injectable({
  providedIn: 'root',
})
export class AccountService {
  private apiServerUrl = environment.apiBaseUrl;
  constructor(private http: HttpClient, private router: Router) {}

  public getAccountAddress(accountId: string): Observable<AccountAddress> {
    let headers = new HttpHeaders().set(
      'Authorization',
      'Bearer ' + sessionStorage.getItem('token')
    );
    return this.http.get<AccountAddress>(
      `${this.apiServerUrl}/accounts/${accountId}/address`,
      { headers: headers }
    );
  }
}
