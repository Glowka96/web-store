import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { CategoryResponse } from 'src/app/models/category-response';
import { CategoryRequest } from 'src/app/models/category-request';
import { CategoryService } from 'src/app/services/products/category.service';
import { take } from 'rxjs';

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

  public addForm = new FormGroup({
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public updateForm = new FormGroup({
    choiceCategory: new FormControl('', {
      updateOn: 'change',
    }),
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public deleteForm = new FormGroup({
    choiceCategory: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {}

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
      const id = this.updateForm.controls['choiceCategory']?.value;
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
      const id = this.deleteForm.controls['choiceCategory']?.value;
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
