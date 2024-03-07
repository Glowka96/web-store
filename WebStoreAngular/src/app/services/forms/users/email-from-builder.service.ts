import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class EmailFromBuilderService {
  private _emailPattern = '^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$';

  constructor(private formBuilder: FormBuilder) {}

  createEmailFormGroup() {
    return this.formBuilder.group({
      email: new FormControl('', {
        validators: [
          Validators.required,
          Validators.pattern(this._emailPattern),
        ],
        updateOn: 'change',
      }),
    });
  }
}
