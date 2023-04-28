import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { Category } from 'src/app/models/category';
import { Subcategory } from 'src/app/models/subcategory';
import { SubcategoryRequest } from 'src/app/models/subcategory-request';
import { SubcategoryService } from 'src/app/services/subcategory.service';

@Component({
  selector: 'app-mod-subcategory',
  templateUrl: './mod-subcategory.component.html',
  styleUrls: ['./mod-subcategory.component.scss'],
})
export class ModSubcategoryComponent implements OnInit {
  @Input()
  categories!: Category[];
  @Input()
  subcategories!: Subcategory[];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';

  public addForm = new FormGroup({
    choiceCategory: new FormControl('', {
      updateOn: 'change',
    }),
    subcategoryName: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public updateForm = new FormGroup({
    choiceCategory: new FormControl('', {
      updateOn: 'change',
    }),
    choiceSubcategory: new FormControl('', {
      updateOn: 'change',
    }),
    subcategoryName: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
  });

  public deleteForm = new FormGroup({
    choiceCategory: new FormControl('', {
      updateOn: 'change',
    }),
  });
  objectData: any;

  constructor(private subcategoryService: SubcategoryService) {}

  ngOnInit(): void {}

  onSumbitAdd() {
    if (this.addForm.valid) {
      let request: SubcategoryRequest = {
        name: this.addForm.controls['subcategoryName']?.value ?? '',
      };
      let id = this.addForm.controls['choiceCategory']?.value;
      if (id) {
        this.subcategoryService.addSubcategory(id, request).subscribe({
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
      let request: SubcategoryRequest = {
        name: this.updateForm.controls['subcategoryName']?.value ?? '',
      };
      let categoryId = this.updateForm.controls['choiceCategory']?.value;
      let subcategoryId = this.updateForm.controls['choiceSubcategory']?.value;
      if (categoryId && subcategoryId) {
        this.subcategoryService
          .updateSubcategory(categoryId, subcategoryId, request)
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
      let id = this.deleteForm.controls['choiceCategory']?.value;
      if (id) {
        this.subcategoryService.deleteSubcategory(id).subscribe({
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
