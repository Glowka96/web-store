import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ResetPasswordService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public sendResetPasswordLink(email: string) {
    const params = new HttpParams().set('email', email);
    return this.http.get<any>(`${this.apiServerUrl}/accounts/reset-password`, {
      params,
    });
  }

  public confirmResetPassword(password: string, token: string) {
    const params = new HttpParams()
      .set('password', password)
      .set('token', token);
    return this.http.patch<any>(
      `${this.apiServerUrl}/accounts/reset-password/confirm`,
      {},
      { params }
    );
  }
}
