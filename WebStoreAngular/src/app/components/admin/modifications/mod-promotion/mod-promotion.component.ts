import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Router } from '@angular/router';
import { take } from 'rxjs';
import { ProductPromotionRequest } from 'src/app/models/products/product-promotion-request';
import { ProductPromotionResponse } from 'src/app/models/products/product-promotion-response';
import { ProductResponse } from 'src/app/models/products/product-response';
import { PromotionFormBuilderService } from 'src/app/services/forms/admins/promotion-form-builder.service';
import { PromotionService } from 'src/app/services/products/promotion.service';

@Component({
  selector: 'app-mod-promotion',
  templateUrl: './mod-promotion.component.html',
  styleUrls: ['./mod-promotion.component.scss'],
})
export class ModPromotionComponent implements OnInit {
  @Input()
  products!: ProductResponse[];
  private errorAddMsg = '';

  public addForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private promotionService: PromotionService,
    private entityFormService: PromotionFormBuilderService
  ) {}

  ngOnInit(): void {
    this.addForm = this.entityFormService.createAddPromotionFormGroup();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: ProductPromotionRequest = {
        productId: this.addForm.controls['choiceProduct']?.value,
        promotionPrice: this.addForm.controls['promotionPrice']?.value,
        startDate: this.addForm.controls['startDate']?.value,
        endDate: this.addForm.controls['endDate']?.value,
      };
      this.promotionService
        .addPromotion(request)
        .pipe(take(1))
        .subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorAddMsg = e.error.errors.join('<br>');
          },
        });
    }
  }

  public get errorAddMessage() {
    return this.errorAddMsg;
  }
}
