import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { take } from 'rxjs';
import { LoginRequest } from 'src/app/models/login-request';
import { RegistrationRequest } from 'src/app/models/registration-request';
import { AuthenticationService } from 'src/app/services/accounts/authentication.service';
import { FormLoginService } from 'src/app/services/accounts/form-login.service';
import { RegistrationService } from 'src/app/services/accounts/registration.service';
import { PasswordFormBuilderService } from 'src/app/services/forms/password-form-builder.service';

@Component({
  selector: 'app-form',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.scss'],
})
export class FormLoginComponent implements OnInit {
  private formSectionMove = false;
  private successMessage?: string | null;
  private errorMessage?: string | null;

  private emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$';

  public loginForm = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.emailPattern)],
      updateOn: 'change',
    }),
    password: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  public registrationForm = this.formBuilder.group({
    firstName: new FormControl('', {
      validators: [
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern('[a-zA-Z ]*'),
        Validators.required,
      ],
      updateOn: 'change',
    }),
    lastName: new FormControl('', {
      validators: [
        Validators.minLength(3),
        Validators.maxLength(20),
        Validators.pattern('[a-zA-Z ]*'),
        Validators.required,
      ],
      updateOn: 'change',
    }),
    email: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.emailPattern)],
      updateOn: 'change',
    }),
    passwordGroup: this.passwordFormControlService.createPasswordFormGroup(),
  });

  constructor(
    private registrationService: RegistrationService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private formService: FormLoginService,
    private passwordFormControlService: PasswordFormBuilderService
  ) {}

  ngOnInit(): void {}

  onSumbitLogin() {
    if (this.loginForm.valid) {
      const request: LoginRequest = {
        email: this.loginForm.controls['email']?.value ?? '',
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
        firstName: this.registrationForm.controls['firstName']?.value ?? '',
        lastName: this.registrationForm.controls['lastName']?.value ?? '',
        email: this.registrationForm.controls['email']?.value ?? '',
        password:
          this.registrationForm.get('passwordGroup.password')?.value ?? '',
      };
      this.registrationService
        .register(request)
        .pipe(take(1))
        .subscribe({
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
