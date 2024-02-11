import { Injectable } from '@angular/core';
import { FormControl, Validators } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class BaseEntityFormBuilderService {
  constructor() {}

  public getNameFormControll() {
    return new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
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
