import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';

@Injectable({
  providedIn: 'root',
})
export class EntityFormBuilderService {
  constructor(
    private formBuilder: FormBuilder,
    private baseEntityFormService: BaseEntityFormBuilderService
  ) {}

  createAddFormGroup() {
    return this.formBuilder.group({
      name: this.baseEntityFormService.getTextFormControll(),
    });
  }

  createUpdateFormGroup() {
    return this.formBuilder.group({
      choice: this.baseEntityFormService.getChoiceFormControll(),
      name: this.baseEntityFormService.getTextFormControll(),
    });
  }

  createDeleteFormGroup() {
    return this.formBuilder.group({
      choice: this.baseEntityFormService.getChoiceFormControll(),
    });
  }
}
