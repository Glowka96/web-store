import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, map } from 'rxjs';
import { __values } from 'tslib';
import { Router } from '@angular/router';
import { LoginRequest } from 'src/app/models/login-request';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private apiServerUrl = environment.apiBaseUrl;
  private _isAuthenticated$ = new BehaviorSubject<boolean>(false);
  public readonly isAuthenticated$ = this._isAuthenticated$.asObservable();

  constructor(private http: HttpClient, private router: Router) {
    this.checkLogged();
  }

  authenticate(request: LoginRequest) {
    return this.http.post<any>(`${this.apiServerUrl}/login`, request).pipe(
      map((userData) => {
        const token = 'Bearer ' + userData.token;
        sessionStorage.setItem('token', token);

        const decodedJWT = this.getDecodedJWT(token);

        sessionStorage.setItem('role', decodedJWT.role);
        sessionStorage.setItem('isLoggedIn', 'true');

        this._isAuthenticated$.next(true);
        this.checkAdminRouteNav();
      })
    );
  }

  logout(): void {
    this.router.navigate([''], {
      queryParams: {},
    });
    const headers = new HttpHeaders().set(
      'Authorization',
      'Bearer ' + sessionStorage.getItem('token')
    );
    this.http
      .post(`${this.apiServerUrl}/logout`, { headers: headers })
      .subscribe(() => {
        sessionStorage.clear();
        this._isAuthenticated$.next(false);
      });
  }

  private checkAdminRouteNav(): void {
    if (sessionStorage.getItem('role') === 'ROLE_ADMIN') {
      this.router.navigate(['/admin-board'], {});
    }
  }

  private checkLogged() {
    const token = sessionStorage.getItem('token');
    if (token) {
      const decodedJWT = this.getDecodedJWT(token);
      sessionStorage.setItem('role', decodedJWT.role);
      sessionStorage.setItem('isLoggedIn', 'true');
      this._isAuthenticated$.next(true);
    }
  }

  private getDecodedJWT(token: string) {
    return JSON.parse(window.atob(token.split('.')[1]));
  }
}
