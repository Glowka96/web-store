import { Injectable } from '@angular/core';
import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';
import { FormBuilder } from '@angular/forms';

@Injectable({
  providedIn: 'root',
})
export class PromotionFormBuilderService {
  constructor(
    private formBuilder: FormBuilder,
    private baseEntityFormService: BaseEntityFormBuilderService
  ) {}

  createAddPromotionFormGroup() {
    return this.formBuilder.group({
      choiceProduct: this.baseEntityFormService.getChoiceFormControll(),
      promotionPrice: this.baseEntityFormService.getPriceFormControll(),
      startDate: this.baseEntityFormService.getChoiceFormControll(),
      endDate: this.baseEntityFormService.getChoiceFormControll(),
    });
  }
}
