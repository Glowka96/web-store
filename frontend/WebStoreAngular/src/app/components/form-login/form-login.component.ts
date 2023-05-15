import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { LoginRequest } from 'src/app/models/login-request';
import { RegistrationRequest } from 'src/app/models/registration-request';
import { AuthenticationService } from 'src/app/services/authentication.service';
import { FormLoginService } from 'src/app/services/form-login.service';
import { RegistrationService } from 'src/app/services/registration.service';

@Component({
  selector: 'app-form',
  templateUrl: './form-login.component.html',
  styleUrls: ['./form-login.component.scss'],
})
export class FormLoginComponent implements OnInit {
  private formSectionMove = false;
  private successMessage?: string | null;
  private errorMessage?: string | null;
  private passwordPattern =
    /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
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

  public registrationForm = this.formBuilder.group(
    {
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
        validators: [
          Validators.required,
          Validators.pattern(this.emailPattern),
        ],
        updateOn: 'change',
      }),
      password: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(this.passwordPattern),
          Validators.minLength(8),
          Validators.maxLength(30),
        ],
      }),
      confirmPassword: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(this.passwordPattern),
          Validators.minLength(8),
          Validators.maxLength(30),
        ],
        updateOn: 'change',
      }),
    },
    {
      validators: this.passwordMatchValidator,
    }
  );

  passwordMatchValidator(formGroup: FormGroup) {
    const passwordControl = formGroup.get('password');
    const confirmPasswordControl = formGroup.get('confirmPassword');

    if (passwordControl?.value !== confirmPasswordControl?.value) {
      confirmPasswordControl?.setErrors({ passwordMatch: true });
    } else {
      confirmPasswordControl?.setErrors(null);
    }
  }

  constructor(
    private registrationService: RegistrationService,
    private authenticationService: AuthenticationService,
    private formBuilder: FormBuilder,
    private formService: FormLoginService
  ) {}

  ngOnInit(): void {}

  onSumbitLogin() {
    if (this.loginForm.valid) {
      let request: LoginRequest = {
        email: this.loginForm.controls['email']?.value ?? '',
        password: this.loginForm.controls['password']?.value ?? '',
      };
      this.authenticationService.authenticate(request).subscribe({
        next: () => {
          this.formService.changeStatusFormLogin();
          this.authenticationService.setLoggedIn(true);
        },
        error: (error) => {
          this.successMessage = null;
          let errorMessage = '';
          errorMessage = error.error.errors.join('<br>');
          this.errorMessage = errorMessage;
        },
      });
    }
  }

  onSubmitRegister() {
    if (this.registrationForm.valid) {
      let request: RegistrationRequest = {
        firstName: this.registrationForm.controls['firstName']?.value,
        lastName: this.registrationForm.controls['lastName']?.value,
        email: this.registrationForm.controls['email']?.value,
        password: this.registrationForm.controls['password'].value,
      };
      this.registrationService.register(request).subscribe({
        next: (response) => {
          this.successMessage = response.message;
          this.errorMessage = null;
        },
        error: (error) => {
          if (error.status === 400) {
            this.successMessage = null;
            let errorMessage = '';
            errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          }
        },
      });
    }
  }

  onSignup() {
    let slider = document.querySelector('.slider');
    slider?.classList.add('moveslider');
    this.formSectionMove = true;
    this.errorMessage = null;
    this.successMessage = null;
    this.loginForm.reset();
  }

  onLogin() {
    let slider = document.querySelector('.slider');
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
