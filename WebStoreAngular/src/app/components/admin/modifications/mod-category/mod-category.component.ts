import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CategoryResponse } from 'src/app/models/products/category-response';
import { CategoryRequest } from 'src/app/models/products/category-request';
import { CategoryService } from 'src/app/services/products/category.service';
import { take } from 'rxjs';
import { EntityFormBuilderService } from 'src/app/services/forms/admins/entity-form-builder.service';

@Component({
  selector: 'app-mod-category',
  templateUrl: './mod-category.component.html',
  styleUrls: ['./mod-category.component.scss'],
})
export class ModCategoryComponent implements OnInit {
  @Input()
  categories!: CategoryResponse[];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm!: FormGroup;
  public updateForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private categoryService: CategoryService,
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
      this.categoryService
        .addCategory(request)
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
        this.categoryService
          .updateCategory(id, request)
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
        this.categoryService
          .deleteCategory(id)
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

  public get listEntity() {
    return this.categories;
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
