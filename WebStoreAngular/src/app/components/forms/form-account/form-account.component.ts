import { Component, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { AccountRequest } from 'src/app/models/account-request';
import { AccountService } from 'src/app/services/accounts/account.service';
import { PasswordFormBuilderService } from 'src/app/services/forms/password-form-builder.service';

@Component({
  selector: 'app-form-account',
  templateUrl: './form-account.component.html',
  styleUrls: ['./form-account.component.scss'],
})
export class FormAccountComponent implements OnInit {
  private errorMessage!: string;
  private imageUrlPattern = /https?:\/\/.*\.(?:png|jpg)/;

  public accountForm = this.formBuilder.group({
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
    passwordGroup: this.passwordFormControlService.createPasswordFormGroup(),
    imageUrl: new FormControl('', {
      validators: [Validators.pattern(this.imageUrlPattern)],
    }),
    updateOn: 'change',
  });

  constructor(
    private accountService: AccountService,
    private formBuilder: FormBuilder,
    private router: Router,
    private passwordFormControlService: PasswordFormBuilderService
  ) {}

  ngOnInit(): void {}

  onSumbitUpdate() {
    if (this.accountForm.valid) {
      const request: AccountRequest = {
        firstName: this.accountForm.controls['firstName']?.value ?? '',
        lastName: this.accountForm.controls['lastName']?.value ?? '',
        password: this.accountForm.get('passwordGroup.password')?.value ?? '',
        imageUrl: this.accountForm.controls['imageUrl']?.value ?? '',
      };
      this.accountService
        .updateAccount(request)
        .pipe(take(1))
        .subscribe({
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
