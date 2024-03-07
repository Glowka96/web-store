import { Injectable } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class BaseEntityFormBuilderService {
  private pricePattern = '^[1-9]+(.[0-9]{1,2})*$';

  constructor() {}

  public getTextFormControll(maxLength: number = 20) {
    return new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(maxLength),
      ],
      updateOn: 'change',
    });
  }

  public getPriceFormControll() {
    return new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.pricePattern)],
      updateOn: 'change',
    });
  }

  public getChoiceFormControll() {
    return new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    });
  }
}
