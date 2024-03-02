import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ProducerResponse } from 'src/app/models/products/producer-response';
import { ProductResponse } from 'src/app/models/products/product-response';
import { ProductRequest } from 'src/app/models/products/product-request';
import { SubcategoryResponse } from 'src/app/models/products/subcategory-response';
import { ProductService } from 'src/app/services/products/product.service';
import { take } from 'rxjs/internal/operators/take';
import { Router } from '@angular/router';
import { ProductTypeResponse } from 'src/app/models/products/product-type-response';
import { ProductFromBuilderService } from 'src/app/services/forms/admins/product-from-builder.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-mod-products',
  templateUrl: './mod-products.component.html',
  styleUrls: ['./mod-products.component.scss'],
})
export class ModProductsComponent implements OnInit {
  @Input()
  subcategories!: SubcategoryResponse[];
  @Input()
  productTypes!: ProductTypeResponse[];
  @Input()
  producers!: ProducerResponse[];
  private _products: ProductResponse[] = [];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';
  private subscription!: Subscription;

  public addForm!: FormGroup;
  public updateForm!: FormGroup;
  public deleteForm!: FormGroup;

  constructor(
    private productService: ProductService,
    private productFormService: ProductFromBuilderService
  ) {
    this.subscription = productService
      .getAllProducts()
      .pipe(take(1))
      .subscribe((products) => {
        this._products = products;
      });
  }

  ngOnInit(): void {
    this.addForm = this.productFormService.createAddFormGroup();
    this.updateForm = this.productFormService.createUpdateFormGroup();
    this.deleteForm = this.productFormService.createDeleteFormGroup();
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  onSumbitAdd() {
    if (this.addForm.valid) {
      const request: ProductRequest = {
        name: this.addForm.controls['name']?.value ?? '',
        description: this.addForm.controls['description']?.value ?? '',
        imageUrl: this.addForm.controls['imageUrl']?.value ?? '',
        price: parseFloat(
          this.addForm.controls['price']?.value?.toString() ?? ''
        ),
        quantity: this.addForm.controls['quantity']?.value ?? '',
        productTypeId: this.addForm.controls['choiceType']?.value ?? '',
      };
      console.log(request);
      const subcategoryId = this.addForm.controls['choiceSubcategory']?.value;
      const producerId = this.addForm.controls['choiceProducer']?.value;
      if (subcategoryId && producerId) {
        this.productService
          .addProduct(subcategoryId, producerId, request)
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
      const request: ProductRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
        description: this.updateForm.controls['description']?.value ?? '',
        imageUrl: this.updateForm.controls['imageUrl']?.value ?? '',
        price: parseFloat(
          this.updateForm.controls['price']?.value?.toString() ?? ''
        ),
        quantity: this.updateForm.controls['quantity']?.value ?? '',
        productTypeId: this.updateForm.controls['choiceType']?.value ?? '',
      };
      const subcategoryId =
        this.updateForm.controls['choiceSubcategory']?.value;
      const producerId = this.updateForm.controls['choiceProducer']?.value;
      if (subcategoryId && producerId) {
        this.productService
          .updateProduct(subcategoryId, producerId, request)
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
      const id = this.deleteForm.controls['choiceProduct']?.value;
      if (id) {
        this.productService
          .deleteProduct(id)
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

  public get products() {
    return this._products;
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
