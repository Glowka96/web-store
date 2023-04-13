import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { LoginRequest } from '../models/login-request';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, map } from 'rxjs';
import { __values } from 'tslib';

@Injectable({
  providedIn: 'root',
})
export class AuthenticationService {
  private apiServerUrl = environment.apiBaseUrl;
  private loggedRole!: BehaviorSubject<string>;
  private loggedIn: BehaviorSubject<boolean> = new BehaviorSubject<boolean>(
    false
  );

  constructor(private http: HttpClient) {}

  authenticate(request: LoginRequest) {
    return this.http.post<any>(`${this.apiServerUrl}/login`, request).pipe(
      map((userData) => {
        sessionStorage.setItem('username', request.email);

        let tokenStr = 'Bearer ' + userData.token;
        sessionStorage.setItem('token', tokenStr);

        let decodedJWT = JSON.parse(window.atob(tokenStr.split('.')[1]));
        this.loggedRole = decodedJWT.role;

        let headers = new HttpHeaders();
        if (tokenStr) {
          console.log('setup headers');
          headers.set('Authorization', tokenStr);
          console.log(headers.get('Authorization'));
          console.log('end setup');
        }
      })
    );
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

  logout(): void {
    let headers = new HttpHeaders().set(
      'Authorization',
      'Bearer ' + sessionStorage.getItem('token')
    );
    this.http
      .post(`${this.apiServerUrl}/logout`, { headers: headers })
      .subscribe(() => {
        this.loggedIn.next(false);
        sessionStorage.removeItem('token');
      });

    // // Send an HTTP GET request to the logout endpoint in your backend API
    // this.http.post(`${this.apiServerUrl}/logout`, this.http.options).subscribe(
    //   () => {
    //     // Logout successful, update your app's state or perform other actions
    //     this.loggedIn.next(false);
    //     sessionStorage.removeItem('token');

    //     window.location.reload();
    //   },
    //   (error) => {
    //     // Handle error if logout request fails
    //     console.error('Logout failed:', error);
    //   }
    // );
  }

  isAuthenticated(): boolean {
    return !!localStorage.getItem('token');
  }
}
