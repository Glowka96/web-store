import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';
import { take } from 'rxjs';
import { ResetPasswordRequest } from 'src/app/models/accounts/reset-password-request';
import { ResetPasswordService } from 'src/app/services/accounts/reset-password.service';
import { PasswordFormBuilderService } from 'src/app/services/forms/users/password-form-builder.service';

@Component({
  selector: 'app-confirm-reset-password',
  templateUrl: './confirm-reset-password.component.html',
  styleUrls: ['./confirm-reset-password.component.scss'],
})
export class ConfirmResetPasswordComponent implements OnInit {
  private token!: string;
  private resposneMessage!: string;
  public confirmResetPasswordForm!: FormGroup;

  constructor(
    private resetPasswordService: ResetPasswordService,
    private route: ActivatedRoute,
    private router: Router,
    private passwordFormBuilderService: PasswordFormBuilderService
  ) {
    this.confirmResetPasswordForm =
      passwordFormBuilderService.createPasswordFormGroup();
  }

  ngOnInit(): void {
    this.route.queryParams.pipe(take(1)).subscribe((params) => {
      this.token = params['token'];
    });
  }

  onSubmitConfirmResetPassword() {
    if (this.confirmResetPasswordForm.valid) {
      const request: ResetPasswordRequest = {
        password: this.confirmResetPasswordForm.get('password')?.value,
      };
      this.resetPasswordService
        .confirmResetPassword(request, this.token)
        .subscribe({
          next: (response) => {
            this.resposneMessage = response.message;
            setTimeout(() => {
              this.router.navigate(['/']);
            }, 3500);
          },
          error: (error) => {
            const errorMessage = error.error.errors.join('<br>');
            this.resposneMessage = errorMessage;
          },
        });
    }
  }

  public get responseMsg() {
    return this.resposneMessage;
  }
}
