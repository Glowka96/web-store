import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ResetPasswordRequest } from 'src/app/models/accounts/reset-password-request';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ResetPasswordService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public sendResetPasswordLink(email: string) {
    const params = new HttpParams().set('email', email);
    return this.http.get<any>(`${this.apiServerUrl}/reset-password`, {
      params,
    });
  }

  public confirmResetPassword(request: ResetPasswordRequest, token: string) {
    const params = new HttpParams().set('token', token);
    return this.http.patch<any>(
      `${this.apiServerUrl}/reset-password/confirm`,
      request,
      { params }
    );
  }
}
