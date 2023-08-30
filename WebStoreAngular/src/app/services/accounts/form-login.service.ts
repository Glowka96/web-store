import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FormLoginService {
  private isActiveFormLogin = false;
  private formLogin = new BehaviorSubject<boolean>(this.isActiveFormLogin);
  constructor() {}

  public changeStatusFormLogin() {
    this.isActiveFormLogin = !this.isActiveFormLogin;
    this.formLogin.next(this.isActiveFormLogin);
  }

  public getIsFromLogin$(): Observable<boolean> {
    return this.formLogin;
  }
}
