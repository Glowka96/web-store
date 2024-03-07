import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { UserFullnameFormBuilderService } from './user-fullname-form-builder.service';
import { PasswordFormBuilderService } from './password-form-builder.service';
import { imageUrlPattern } from '../pattern-constants';

@Injectable({
  providedIn: 'root',
})
export class AccountFormBuilderService {
  private imageUrlPattern = imageUrlPattern;

  constructor(
    private userFullnameFormService: UserFullnameFormBuilderService,
    private passwordFormService: PasswordFormBuilderService,
    private formBuilder: FormBuilder
  ) {}

  createAccountFormGroup() {
    return this.formBuilder.group({
      fullnameGroup: this.userFullnameFormService.createUserFullnameFormGroup(),
      passwordGroup: this.passwordFormService.createPasswordFormGroup(),
      imageUrl: new FormControl('', {
        validators: [Validators.pattern(this.imageUrlPattern)],
      }),
      updateOn: 'change',
    });
  }
}
