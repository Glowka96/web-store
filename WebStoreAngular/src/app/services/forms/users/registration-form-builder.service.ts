import { Injectable } from '@angular/core';
import { EmailFromBuilderService } from './email-from-builder.service';
import {
  FormBuilder,
  FormControl,
  FormGroup,
  Validators,
} from '@angular/forms';
import { PasswordFormBuilderService } from './password-form-builder.service';
import { UserFullnameFormBuilderService } from './user-fullname-form-builder.service';

@Injectable({
  providedIn: 'root',
})
export class RegistrationFormBuilderService {
  constructor(
    private emailFormBuilderService: EmailFromBuilderService,
    private userFullnameFormBuilderService: UserFullnameFormBuilderService,
    private passwordFormBuilderService: PasswordFormBuilderService,
    private formBuilder: FormBuilder
  ) {}

  createRegistrationFormBuilder(): FormGroup {
    return this.formBuilder.group({
      fullnameGroup:
        this.userFullnameFormBuilderService.createUserFullnameFormGroup(),
      emailGroup: this.emailFormBuilderService.createEmailFormGroup(),
      passwordGroup: this.passwordFormBuilderService.createPasswordFormGroup(),
    });
  }
}
