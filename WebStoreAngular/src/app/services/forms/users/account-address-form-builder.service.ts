import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class AccountAddressFormBuilderService {
  private postcodePattern = /^\d{2}-\d{3}$/;
  private addressPattern =
    /^(ul.?\s)?[A-Z]?[a-z]+\s\d{1,3}((\/\d{1,3})?|(\sm.?\s)\d{1,3})[a-z]?$/;

  constructor(private formBuilder: FormBuilder) {}

  createAccountAddressFormGroup() {
    return this.formBuilder.group({
      city: new FormControl('', {
        validators: [
          Validators.required,
          Validators.min(2),
          Validators.max(32),
          Validators.pattern('[a-zA-z]+'),
        ],
        updateOn: 'change',
      }),
      postcode: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(this.postcodePattern),
        ],
        updateOn: 'change',
      }),
      street: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(this.addressPattern),
        ],
        updateOn: 'change',
      }),
    });
  }
}
