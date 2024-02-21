import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class UserFullnameFormBuilderService {
  private _namePattern = '^[a-zA-Z]+$';

  constructor(private formBuilder: FormBuilder) {}

  createUserFullnameFormGroup() {
    return this.formBuilder.group({
      firstName: new FormControl('', {
        validators: [
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern(this._namePattern),
          Validators.required,
        ],
        updateOn: 'change',
      }),
      lastName: new FormControl('', {
        validators: [
          Validators.minLength(3),
          Validators.maxLength(20),
          Validators.pattern(this._namePattern),
          Validators.required,
        ],
        updateOn: 'change',
      }),
    });
  }
}
