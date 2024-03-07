import { Injectable } from '@angular/core';
import { EmailFromBuilderService } from './email-from-builder.service';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class LoginFormBuilderService {
  constructor(
    private emailFormService: EmailFromBuilderService,
    private formBuilder: FormBuilder
  ) {}

  createLoginFormGroup() {
    return this.formBuilder.group({
      emailGroup: this.emailFormService.createEmailFormGroup(),
      password: new FormControl('', {
        validators: [Validators.required],
        updateOn: 'change',
      }),
    });
  }
}
