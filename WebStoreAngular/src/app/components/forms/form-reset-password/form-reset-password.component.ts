import { Component, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { take } from 'rxjs';
import { ResetPasswordService } from 'src/app/services/accounts/reset-password.service';
import { EmailFromBuilderService } from 'src/app/services/forms/users/email-from-builder.service';

@Component({
  selector: 'app-form-reset-password',
  templateUrl: './form-reset-password.component.html',
  styleUrls: ['./form-reset-password.component.scss'],
})
export class FormResetPasswordComponent implements OnInit {
  private responseMessage?: string;
  public resetPasswordForm!: FormGroup;

  constructor(
    private resetPasswordService: ResetPasswordService,
    private emailFormService: EmailFromBuilderService
  ) {}

  ngOnInit(): void {
    window.scroll({
      top: 0,
      left: 0,
      behavior: 'smooth',
    });
    this.resetPasswordForm = this.emailFormService.createEmailFormGroup();
  }

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
