import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { PasswordMatchValidatorService } from '../../password-match-validator.service';

@Injectable({
  providedIn: 'root',
})
export class PasswordFormBuilderService {
  private passwordPattern =
    /^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])(?=.*[!@#$%^&*])(?=.{8,30})/;

  constructor(
    private formBuilder: FormBuilder,
    private passwordMatchValidatorService: PasswordMatchValidatorService
  ) {}

  createPasswordFormGroup() {
    return this.formBuilder.group(
      {
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
            Validators.required,
            Validators.pattern(this.passwordPattern),
            Validators.minLength(8),
            Validators.maxLength(30),
          ],
          updateOn: 'change',
        }),
      },
      {
        validators:
          this.passwordMatchValidatorService.validatePasswordMatch.bind(this),
      }
    );
  }
}
