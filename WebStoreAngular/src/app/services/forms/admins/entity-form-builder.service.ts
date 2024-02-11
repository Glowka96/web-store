import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';

@Injectable({
  providedIn: 'root',
})
export class EntityFormBuilderService {
  constructor(
    private formBuilder: FormBuilder,
    private baseEntityForm: BaseEntityFormBuilderService
  ) {}

  createAddFormGroup() {
    return this.formBuilder.group({
      name: this.baseEntityForm.getNameFormControll(),
    });
  }

  createUpdateFormGroup() {
    return this.formBuilder.group({
      choice: this.baseEntityForm.getChoiceFormControll(),
      name: this.baseEntityForm.getNameFormControll(),
    });
  }

  createDeleteFormGroup() {
    return this.formBuilder.group({
      choice: this.baseEntityForm.getChoiceFormControll(),
    });
  }
}
