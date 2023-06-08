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
        let token = 'Bearer ' + userData.token;
        sessionStorage.setItem('token', token);

        let decodedJWT = this.getDecodedJWT(token);
        this.loggedRole.next(decodedJWT.role);
        this.loggedIn.next(true);

        sessionStorage.setItem('id', decodedJWT.id);

        this.checkAdminRouteNav();

        let headers = new HttpHeaders();
        headers.set('Authorization', token);
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
      let decodedJWT = this.getDecodedJWT(token);
      this.loggedRole.next(decodedJWT.role);
      this.loggedIn.next(true);
      sessionStorage.setItem('id', decodedJWT.id);
    }
  }

  private getDecodedJWT(token: string) {
    return JSON.parse(window.atob(token.split('.')[1]));
  }

  public loggedRole$(): Observable<string> {
    return this.loggedRole.asObservable();
  }

  public loggedIn$(): Observable<boolean> {
    return this.loggedIn.asObservable();
  }
}
