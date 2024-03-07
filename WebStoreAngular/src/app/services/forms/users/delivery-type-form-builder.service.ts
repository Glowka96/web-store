import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class DeliveryTypeFormBuilderService {
  constructor(private formBuilder: FormBuilder) {}

  createDeliveryFormGroup() {
    return this.formBuilder.group({
      choice: new FormControl('', {
        validators: [Validators.required],
        updateOn: 'change',
      }),
    });
  }
}
