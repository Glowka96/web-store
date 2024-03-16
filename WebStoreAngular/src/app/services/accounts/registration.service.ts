import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { RegistrationRequest } from 'src/app/models/accounts/registration-request';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  register(request: RegistrationRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/registration`, request);
  }

  confirmAccount(token: string): Observable<any> {
    return this.http.get<any>(
      `${this.apiServerUrl}/registration/confirm?token=${token}`
    );
  }
}
