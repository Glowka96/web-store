import { Component } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { take } from 'rxjs';
import { ResetPasswordService } from 'src/app/services/accounts/reset-password.service';

@Component({
  selector: 'app-form-reset-password',
  templateUrl: './form-reset-password.component.html',
  styleUrls: ['./form-reset-password.component.scss'],
})
export class FormResetPasswordComponent {
  private responseMessage?: string;
  private emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$';

  public resetPasswordForm = new FormGroup({
    email: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.emailPattern)],
      updateOn: 'change',
    }),
  });

  constructor(private resetPasswordService: ResetPasswordService) {}

  public onSumbitResetPassword() {
    if (this.resetPasswordForm.valid) {
      const email = this.resetPasswordForm.controls['email'].value ?? '';
      this.resetPasswordService
        .sendResetPasswordLink(email)
        .pipe(take(1))
        .subscribe({
          next: (response) => {
            this.responseMessage = response.message;
          },
          error: (error) => {
            this.responseMessage = error.error.errors.join('<br>');
          },
        });
    }
  }

  public get responseMsg() {
    return this.responseMessage;
  }
}
