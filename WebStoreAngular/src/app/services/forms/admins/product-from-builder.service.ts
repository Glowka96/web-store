import { Injectable } from '@angular/core';
import { FormBuilder, FormControl, Validators } from '@angular/forms';
import { BaseEntityFormBuilderService } from './base-entity-form-builder.service';
import { imageUrlPattern } from '../pattern-constants';

@Injectable({
  providedIn: 'root',
})
export class ProductFromBuilderService {
  private imageUrlPattern = imageUrlPattern;

  constructor(
    private formBuilder: FormBuilder,
    private baseEntityFormService: BaseEntityFormBuilderService
  ) {}

  createAddFormGroup() {
    return this.formBuilder.group({
      choiceSubcategory: this.baseEntityFormService.getChoiceFormControll(),
      choiceProducer: this.baseEntityFormService.getChoiceFormControll(),
      name: this.baseEntityFormService.getTextFormControll(32),
      description: this.getDescriptionFormControll(),
      imageUrl: this.getImageUrlFormControll(),
      price: this.baseEntityFormService.getPriceFormControll(),
      quantity: this.getQuanityFormControll(),
      choiceType: this.baseEntityFormService.getChoiceFormControll(),
    });
  }

  createUpdateFormGroup() {
    return this.formBuilder.group({
      choiceSubcategory: this.baseEntityFormService.getChoiceFormControll(),
      choiceProducer: this.baseEntityFormService.getChoiceFormControll(),
      choiceProduct: this.baseEntityFormService.getChoiceFormControll(),
      name: this.baseEntityFormService.getTextFormControll(32),
      description: this.getDescriptionFormControll(),
      imageUrl: this.getImageUrlFormControll(),
      price: this.baseEntityFormService.getPriceFormControll(),
      quantity: this.getQuanityFormControll(),
      choiceType: this.baseEntityFormService.getChoiceFormControll(),
    });
  }

  createDeleteFormGroup() {
    return this.formBuilder.group({
      choiceProduct: this.baseEntityFormService.getChoiceFormControll(),
    });
  }

  private getDescriptionFormControll() {
    return new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(512),
      ],
      updateOn: 'change',
    });
  }

  private getImageUrlFormControll() {
    return new FormControl('', {
      validators: [
        Validators.required,
        Validators.pattern(this.imageUrlPattern),
      ],
      updateOn: 'change',
    });
  }

  private getQuanityFormControll() {
    return new FormControl('', {
      validators: [Validators.required, Validators.pattern('^[0-9]*$')],
      updateOn: 'change',
    });
  }
}
