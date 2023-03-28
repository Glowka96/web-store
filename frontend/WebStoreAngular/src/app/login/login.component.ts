import { Component, Input, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { RegistrationService } from '../services/registration.service';
import { RegistrationRequest } from '../models/registration-request';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss'],
})
export class LoginComponent implements OnInit {
  @Input()
  opacity!: number;
  formSectionMove = false;
  registrationError!: string;
  snackBar: any;
  router: any;
  successMessage?: string;
  errorMessage?: string;

  public loginForm = new FormGroup({
    email: new FormControl(null, [Validators.required]),
    password: new FormControl(null, [Validators.required]),
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
      email: new FormControl('', [
        Validators.required,
        Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$'),
      ]),
      password: new FormControl('', [Validators.required]),
      confirmPassword: ['', { validators: [Validators.required] }],
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
    private formBuilder: FormBuilder
  ) {}

  ngOnInit(): void {}

  onSubmit() {
    if (this.registrationForm.valid) {
      const request: RegistrationRequest = {
        firstName: this.registrationForm.controls['firstName'].value || '',
        lastName: this.registrationForm.controls['lastName'].value || '',
        email: this.registrationForm.controls['email'].value || '',
        password: this.registrationForm.controls['password'].value || '',
      };
      console.log('create-request');
      this.registrationService.register(this.registrationForm.value).subscribe(
        (response) => {
          this.successMessage = response.message;
          this.errorMessage != null;
        },
        (error) => {
          if (error.status === 400) {
            this.successMessage != null;
            let errorMessage = '';
            errorMessage = error.error.errors.join('<br>');
            this.errorMessage = errorMessage;
          }
        }
      );
    }
  }

  onSignup() {
    let slider = document.querySelector('.slider');
    slider?.classList.add('moveslider');
    this.formSectionMove = true;
  }

  onLogin() {
    let slider = document.querySelector('.slider');
    slider?.classList.remove('moveslider');
    this.formSectionMove = false;
  }
}
