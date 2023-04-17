import { Injectable } from '@angular/core';
import {
  HttpErrorResponse,
  HttpEvent,
  HttpHandler,
  HttpInterceptor,
  HttpRequest,
} from '@angular/common/http';
import { Observable, catchError, throwError } from 'rxjs';
import { RouterLink } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthHttpInterceptorService implements HttpInterceptor {
  constructor() {}
  intercept(
    request: HttpRequest<any>,
    next: HttpHandler
  ): Observable<HttpEvent<any>> {
    let token = sessionStorage.getItem('token');
    console.log('http interceptor ' + token);
    if (token) {
      request = request.clone({
        setHeaders: { Authorization: `${token}` },
      });
    }

    return next.handle(request).pipe(
      catchError((err) => {
        if (err instanceof HttpErrorResponse) {
          if (err.status === 401) {
            // redirect user to the logout page
          }
        }
        const erre = new Error('test');
        return throwError(() => erre);
      })
    );
  }
}
