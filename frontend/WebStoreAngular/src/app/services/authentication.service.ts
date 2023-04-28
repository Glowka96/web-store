import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../models/login-request';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { __values } from 'tslib';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private apiServerUrl = environment.apiBaseUrl;
  private loggedRole: BehaviorSubject<string> = new BehaviorSubject<string>('');
  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );

  constructor(private http: HttpClient, private router: Router) {
    this.checkLogged();
  }

  authenticate(request: LoginRequest) {
    return this.http.post<any>(`${this.apiServerUrl}/login`, request).pipe(
      map((userData) => {
        sessionStorage.setItem('username', request.email);

        let tokenStr = 'Bearer ' + userData.token;
        sessionStorage.setItem('token', tokenStr);

        let decodedJWT = JSON.parse(window.atob(tokenStr.split('.')[1]));
        this.loggedRole.next(decodedJWT.role);

        this.checkAdminRouteNav();

        let headers = new HttpHeaders();
        if (tokenStr) {
          headers.set('Authorization', tokenStr);
        }
      })
    );
  }

  logout(): void {
    this.router.navigate([''], {
      queryParams: {},
      queryParamsHandling: 'merge',
    });
    let headers = new HttpHeaders().set(
      'Authorization',
      'Bearer ' + sessionStorage.getItem('token')
    );
    this.http
      .post(`${this.apiServerUrl}/logout`, { headers: headers })
      .subscribe(() => {
        this.loggedIn.next(false);
        this.loggedRole.next('');
        sessionStorage.clear();
      });
  }

  private checkAdminRouteNav(): void {
    if (this.loggedRole.value === 'ROLE_ADMIN') {
      this.router.navigate(['/admin-board'], {});
    }
  }

  private checkLogged() {
    let token = sessionStorage.getItem('token');
    if (token) {
      let decodedJWT = JSON.parse(window.atob(token.split('.')[1]));
      this.loggedRole.next(decodedJWT.role);
      this.loggedIn.next(true);
    }
  }

  isLoggedRole(): Observable<string> {
    return this.loggedRole.asObservable();
  }

  setLoggedIn(loggedIn: boolean): void {
    this.loggedIn.next(loggedIn);
  }

  isLoggedIn(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }
}
