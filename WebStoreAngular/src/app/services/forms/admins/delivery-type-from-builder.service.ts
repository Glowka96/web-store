import { Injectable } from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';

@Injectable({
  providedIn: 'root',
})
export class DeliveryTypeFromBuilderService {
  constructor(
    private formBuilder: FormBuilder,
    private baseEntityFormService: BaseEntityFormBuilderService
  ) {}

  createAddFormGroup() {
    return this.formBuilder.group({
      name: this.baseEntityFormService.getTextFormControll(32),
      price: this.baseEntityFormService.getPriceFormControll(),
    });
  }

  createDeleteFormGroup() {
    return this.formBuilder.group({
      choice: this.baseEntityFormService.getChoiceFormControll(),
    });
  }
}
