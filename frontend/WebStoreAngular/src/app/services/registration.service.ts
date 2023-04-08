import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { catchError, map, Observable, tap, throwError } from 'rxjs';
import { environment } from 'src/environments/environment';
import { RegistrationRequest } from '../models/registration-request';

@Injectable({
  providedIn: 'root',
})
export class RegistrationService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  register(request: any): Observable<any> {
    return this.http
      .post<any>(`${this.apiServerUrl}/registration`, request)
      .pipe(
        tap((response) => console.log('Server response:', response))
        //catchError(this.handleError)
      );
  }
}
