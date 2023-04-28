import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Category } from 'src/app/models/category';
import { CategoryRequest } from 'src/app/models/category-request';
import { CategoryService } from 'src/app/services/category.service';

@Component({
  selector: 'app-mod-category',
  templateUrl: './mod-category.component.html',
  styleUrls: ['./mod-category.component.scss'],
})
export class ModCategoryComponent implements OnInit {
  @Input()
  categories!: Category[];
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
      updateOn: 'change',
    }),
  });

  constructor(private categoryService: CategoryService) {}

  ngOnInit(): void {}

  onSumbitAdd() {
    if (this.addForm.valid) {
      let request: CategoryRequest = {
        name: this.addForm.controls['name']?.value ?? '',
      };
      this.categoryService.addCategory(request).subscribe({
        next: () => window.location.reload(),
        error: (e) => {
          this.errorAddMsg = e.error.errors.join('<br>');
        },
      });
    }
  }

  onSumbitUpdate() {
    if (this.updateForm.valid) {
      let request: CategoryRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
      };
      let id = this.updateForm.controls['choiceCategory']?.value;
      if (id) {
        this.categoryService.updateCategory(id, request).subscribe({
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
      let id = this.deleteForm.controls['choiceCategory']?.value;
      if (id) {
        this.categoryService.deleteCategory(id).subscribe({
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
