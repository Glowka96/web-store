import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class FormLoginService {
  private isActiveFormLogin = false;
  private opacity: number = 1;
  private opacitySubject = new BehaviorSubject<number>(this.opacity);
  constructor() {}

  public changeStatusFormLogin() {
    this.isActiveFormLogin = !this.isActiveFormLogin;
    this.isActiveFormLogin ? (this.opacity = 0.1) : (this.opacity = 1);
    this.opacitySubject.next(this.opacity);
  }

  public getOpacitySubject() {
    return this.opacitySubject;
  }

  public setOpacity(opacity: number) {
    this.opacitySubject.next(opacity);
    this.opacity = opacity;
  }
}
