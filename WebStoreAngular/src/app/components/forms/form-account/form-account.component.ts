import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AccountRequest } from 'src/app/models/account-request';
import { AccountService } from 'src/app/services/accounts/account.service';

@Component({
  selector: 'app-form-account',
  templateUrl: './form-account.component.html',
  styleUrls: ['./form-account.component.scss'],
})
export class FormAccountComponent implements OnInit {
  private accountId!: string;
  private errorMessage!: string;
  private passwordPattern =
    /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,})/;
  private imageUrlPattern = /https?:\/\/.*\.(?:png|jpg)/;

  public accountForm = this.formBuilder.group(
    {
      firstName: new FormControl('', {
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern('[a-zA-Z]+'),
        ],
        updateOn: 'change',
      }),
      lastName: new FormControl('', {
        validators: [
          Validators.required,
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern('[a-zA-Z]+'),
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
        updateOn: 'change',
      }),
      confirmPassword: new FormControl('', {
        validators: [
          Validators.pattern(this.passwordPattern),
          Validators.minLength(8),
          Validators.maxLength(30),
        ],
        updateOn: 'change',
      }),
      imageUrl: new FormControl('', {
        validators: [Validators.pattern(this.imageUrlPattern)],
      }),
      updateOn: 'change',
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
    private accountService: AccountService,
    private formBuilder: FormBuilder,
    private router: Router
  ) {
    this.accountId != sessionStorage.getItem('id');
  }

  ngOnInit(): void {}

  onSumbitUpdate() {
    if (this.accountForm.valid) {
      const request: AccountRequest = {
        firstName: this.accountForm.controls['firstName']?.value,
        lastName: this.accountForm.controls['lastName']?.value,
        password: this.accountForm.controls['password']?.value,
        imageUrl: this.accountForm.controls['imageUrl']?.value,
      };
      this.accountService.updateAccount(this.accountId, request).subscribe({
        next: () => {
          this.router.navigate(['/accounts'], {});
        },
        error: (error) => {
          const errorMessage = error.error.errors.join('<br>');
          this.errorMessage = errorMessage;
        },
      });
    }
  }

  public get getErrorMessage() {
    return this.errorMessage;
  }
}
