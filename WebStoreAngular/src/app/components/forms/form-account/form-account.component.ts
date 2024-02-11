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
import { AccountFormBuilderService } from 'src/app/services/forms/users/account-form-builder.service';
import { PasswordFormBuilderService } from 'src/app/services/forms/users/password-form-builder.service';

@Component({
  selector: 'app-form-account',
  templateUrl: './form-account.component.html',
  styleUrls: ['./form-account.component.scss'],
})
export class FormAccountComponent implements OnInit {
  private errorMessage!: string;

  public accountForm!: FormGroup;

  constructor(
    private accountService: AccountService,
    private router: Router,
    private accountFormService: AccountFormBuilderService
  ) {}

  ngOnInit(): void {
    this.accountForm = this.accountFormService.createAccountFormGroup();
  }

  onSumbitUpdate() {
    if (this.accountForm.valid) {
      const request: AccountRequest = {
        firstName: this.accountForm.get('passwordGroup.firstName')?.value ?? '',
        lastName: this.accountForm.get('passwordGroup.lastName')?.value ?? '',
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
