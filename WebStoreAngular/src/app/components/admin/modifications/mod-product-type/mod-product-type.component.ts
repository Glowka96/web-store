import { Component, Input } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { take } from 'rxjs';
import { CategoryRequest } from 'src/app/models/products/category-request';
import { ProductTypeResponse } from 'src/app/models/products/product-type-response';
import { EntityFormBuilderService } from 'src/app/services/forms/admins/entity-form-builder.service';
import { ProductTypeService } from 'src/app/services/products/product-type.service';

@Component({
  selector: 'app-mod-product-type',
  templateUrl: './mod-product-type.component.html',
  styleUrls: ['./mod-product-type.component.scss'],
})
export class ModProductTypeComponent {
  @Input()
  productTypes!: ProductTypeResponse[];

  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm!: FormGroup;
  public updateForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private productTypeService: ProductTypeService,
    private entityFormService: EntityFormBuilderService
  ) {}

  ngOnInit(): void {
    this.addForm = this.entityFormService.createAddFormGroup();
    this.updateForm = this.entityFormService.createUpdateFormGroup();
    this.deleteForm = this.entityFormService.createDeleteFormGroup();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: CategoryRequest = {
        name: this.addForm.controls['name']?.value ?? '',
      };
      this.productTypeService
        .addProductType(request)
        .pipe(take(1))
        .subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorAddMsg = e.error.errors.join('<br>');
          },
        });
    }
  }

  onSumbitUpdate() {
    if (this.updateForm.valid) {
      const request: CategoryRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
      };
      const id = this.updateForm.controls['choice']?.value;
      if (id) {
        this.productTypeService
          .updateProductType(id, request)
          .pipe(take(1))
          .subscribe({
            next: () => window.location.reload(),
            error: (e) => {
              this.errorAddMsg = e.error.errors.join('<br>');
            },
          });
      }
    }
  }

  onSumbitDelete() {
    if (this.deleteForm.valid) {
      const id = this.deleteForm.controls['choice']?.value;
      if (id) {
        this.productTypeService
          .deleteProductType(id)
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

  public get errorAddMessage() {
    return this.errorAddMsg;
  }

  public get errorUpdateMessage() {
    return this.errorUpdateMsg;
  }

  public get errorDeleteMessage() {
    return this.errorDeleteMsg;
  }
}
