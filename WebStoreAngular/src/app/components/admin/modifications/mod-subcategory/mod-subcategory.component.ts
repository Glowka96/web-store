import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CategoryResponse } from 'src/app/models/products/category-response';
import { SubcategoryResponse } from 'src/app/models/products/subcategory-response';
import { SubcategoryRequest } from 'src/app/models/products/subcategory-request';
import { SubcategoryService } from 'src/app/services/products/subcategory.service';
import { take } from 'rxjs/internal/operators/take';
import { SubcategoryFormBuilderService } from 'src/app/services/forms/admins/subcategory-form-builder.service';

@Component({
  selector: 'app-mod-subcategory',
  templateUrl: './mod-subcategory.component.html',
  styleUrls: ['./mod-subcategory.component.scss'],
})
export class ModSubcategoryComponent implements OnInit {
  @Input()
  categories!: CategoryResponse[];
  @Input()
  subcategories!: SubcategoryResponse[];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm!: FormGroup;
  public updateForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private subcategoryService: SubcategoryService,
    private subcategoryFormService: SubcategoryFormBuilderService
  ) {}

  ngOnInit(): void {
    this.addForm = this.subcategoryFormService.createAddFormGroup();
    this.updateForm = this.subcategoryFormService.createUpdateFormGroup();
    this.deleteForm = this.subcategoryFormService.createDeleteFormGroup();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: SubcategoryRequest = {
        name: this.addForm.controls['name']?.value ?? '',
      };
      const id = this.addForm.controls['choiceCategory']?.value;
      if (id) {
        this.subcategoryService
          .addSubcategory(id, request)
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

  onSumbitUpdate() {
    if (this.updateForm.valid) {
      const request: SubcategoryRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
      };
      const categoryId = this.updateForm.controls['choiceCategory']?.value;
      const subcategoryId =
        this.updateForm.controls['choiceSubcategory']?.value;
      if (categoryId && subcategoryId) {
        this.subcategoryService
          .updateSubcategory(categoryId, subcategoryId, request)
          .pipe(take(1))
          .subscribe({
            next: () => window.location.reload(),
            error: (e) => {
              this.errorUpdateMsg = e.error.errors.join('<br>');
            },
          });
      }
    }
  }

  onSumbitDelete() {
    if (this.deleteForm.valid) {
      const id = this.deleteForm.controls['choiceSubcategory']?.value;
      if (id) {
        this.subcategoryService
          .deleteSubcategory(id)
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
