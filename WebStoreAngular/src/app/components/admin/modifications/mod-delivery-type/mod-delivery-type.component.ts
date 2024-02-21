import { Component, Input, OnInit } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';
import { take } from 'rxjs/internal/operators/take';
import { DeliveryTypeRequest } from 'src/app/models/orders/delivery-type-request';
import { DeliveryTypeResponse } from 'src/app/models/orders/delivery-type-response';
import { EntityFormBuilderService } from 'src/app/services/forms/admins/entity-form-builder.service';
import { DeliveryTypeService } from 'src/app/services/olders/delivery-type.service';

@Component({
  selector: 'app-mod-delivery-type',
  templateUrl: './mod-delivery-type.component.html',
  styleUrls: ['./mod-delivery-type.component.scss'],
})
export class ModDeliveryTypeComponent implements OnInit {
  private _deliveryTypes!: DeliveryTypeResponse[];
  private errorAddMsg = '';
  private errorDeleteMsg = '';
  private subscription!: Subscription;

  public addForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private deliveryTypeService: DeliveryTypeService,
    private entityFormService: EntityFormBuilderService
  ) {
    this.subscription = deliveryTypeService
      .getAllDeliveryType()
      .subscribe((types) => (this._deliveryTypes = types));
  }

  ngOnInit(): void {
    this.addForm = this.entityFormService.createAddFormGroup();
    this.deleteForm = this.entityFormService.createDeleteFormGroup();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: DeliveryTypeRequest = {
        name: this.addForm.controls['name']?.value ?? '',
        price: this.addForm.controls['price']?.value ?? '',
      };
      this.deliveryTypeService
        .saveDeliveryType(request)
        .pipe(take(1))
        .subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorAddMsg = e.error.errors.join('<br>');
          },
        });
    }
  }

  onSumbitDelete() {
    if (this.deleteForm.valid) {
      const id = this.deleteForm.controls['choice']?.value;
      if (id) {
        this.deliveryTypeService
          .deleteDeliveryType(id)
          .pipe(take(1))
          .subscribe({
            next: () => window.location.reload(),
            error: (e) => {
              this.errorDeleteMsg = e.error.errors.join('<br>');
            },
          });
      }
    }
  }

  public get deliveryTypes() {
    console.log(this._deliveryTypes);
    return this._deliveryTypes;
  }

  public get errorAddMessage() {
    return this.errorAddMsg;
  }

  public get errorDeleteMessage() {
    return this.errorDeleteMsg;
  }
}
