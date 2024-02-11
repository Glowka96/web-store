import { trigger } from '@angular/animations';
import { Component, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { take } from 'rxjs';
import { LoginRequest } from 'src/app/models/login-request';
import { RegistrationRequest } from 'src/app/models/registration-request';
import { AuthenticationService } from 'src/app/services/accounts/authentication.service';
import { FormLoginService } from 'src/app/services/accounts/form-login.service';
import { RegistrationService } from 'src/app/services/accounts/registration.service';
import { RegistrationFormBuilderService } from 'src/app/services/forms/users/registration-form-builder.service';

@Component({
  selector: 'app-form',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.scss'],
})
export class FormLoginComponent implements OnInit {
  private formSectionMove = false;
  private successMessage?: string | null;
  private errorMessage?: string | null;

  public loginForm!: FormGroup;
  public registrationForm!: FormGroup;

  constructor(
    private registrationService: RegistrationService,
    private authenticationService: AuthenticationService,
    private formService: FormLoginService
  ) {}

  ngOnInit(): void {
    this.loginForm = this.formService.createLoginForm();
    this.registrationForm = this.formService.createRegisterForm();
  }

  onSumbitLogin() {
    if (this.loginForm.valid) {
      const request: LoginRequest = {
        email: this.loginForm.get('emailGroup.email')?.value ?? '',
        password: this.loginForm.controls['password']?.value ?? '',
      };
      this.authenticationService
        .authenticate(request)
        .pipe(take(1))
        .subscribe({
          next: () => {
            this.formService.changeStatusFormLogin();
          },
          error: (error) => {
            this.successMessage = null;
            const errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          },
        });
    }
  }

  onSubmitRegister() {
    if (this.registrationForm.valid) {
      const request: RegistrationRequest = {
        firstName:
          this.registrationForm.get('fullnameGroup.firstName')?.value ?? '',
        lastName:
          this.registrationForm.get('fullnameGroup.lastName')?.value ?? '',
        email: this.registrationForm.get('emailGroup.email')?.value ?? '',
        password:
          this.registrationForm.get('passwordGroup.password')?.value ?? '',
      };
      this.registrationService.register(request).subscribe({
        next: (response) => {
          this.successMessage = response.message;
          this.errorMessage = null;
        },
        error: (error) => {
          if (error.status === 400) {
            this.successMessage = null;
            const errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          }
        },
      });
    }
  }

  onSignup() {
    const slider = document.querySelector('.slider');
    slider?.classList.add('moveslider');
    this.formSectionMove = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.loginForm.reset();
  }

  onLogin() {
    const slider = document.querySelector('.slider');
    slider?.classList.remove('moveslider');
    this.formSectionMove = false;
    this.successMessage = null;
    this.errorMessage = null;
    this.registrationForm.reset();
  }

  closeLoginForm() {
    this.formService.changeStatusFormLogin();
  }

  public get getSuccessMessage() {
    return this.successMessage;
  }

  public get getErrorMessage() {
    return this.errorMessage;
  }

  public get getFormSectionMove() {
    return this.formSectionMove;
  }
}
