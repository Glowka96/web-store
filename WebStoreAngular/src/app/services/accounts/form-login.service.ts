import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { RegistrationFormBuilderService } from '../forms/users/registration-form-builder.service';
import { LoginFormBuilderService } from '../forms/users/login-form-builder.service';

@Injectable({
  providedIn: 'root',
})
export class FormLoginService {
  private isActiveFormLogin = false;
  private formLogin = new BehaviorSubject<boolean>(this.isActiveFormLogin);
  constructor(
    private registerFormService: RegistrationFormBuilderService,
    private loginFormService: LoginFormBuilderService
  ) {}

  public changeStatusFormLogin() {
    this.isActiveFormLogin = !this.isActiveFormLogin;
    this.formLogin.next(this.isActiveFormLogin);
  }

  public getIsFromLogin$(): Observable<boolean> {
    return this.formLogin;
  }

  public createLoginForm() {
    return this.loginFormService.createLoginFormGroup();
  }

  public createRegisterForm() {
    return this.registerFormService.createRegistrationFormBuilder();
  }
}
