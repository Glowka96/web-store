import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup, Validators } from '@angular/forms';
import { ProducerResponse } from 'src/app/models/producer-response';
import { ProductResponse } from 'src/app/models/product-response';
import { ProductRequest } from 'src/app/models/product-request';
import { SubcategoryResponse } from 'src/app/models/subcategory-response';
import { ProducerService } from 'src/app/services/producer.service';
import { ProductService } from 'src/app/services/product.service';

@Component({
  selector: 'app-mod-products',
  templateUrl: './mod-products.component.html',
  styleUrls: ['./mod-products.component.scss'],
})
export class ModProductsComponent implements OnInit {
  @Input()
  subcategories!: SubcategoryResponse[];
  private productTypes: string[] = [];
  private products: ProductResponse[] = [];
  private producers: ProducerResponse[] = [];
  private errorAddMsg = '';
  private errorUpdateMsg = '';
  private errorDeleteMsg = '';
  private urlPattern =
    '^(http|https)://((w{3}).)?[w]*.[a-z]{2,6}(/w*)+(.[a-z]{3,4})$';
  private pricePattern = '^[1-9]+(.[0-9]{1,2})*$';

  public addForm = new FormGroup({
    choiceSubcategory: new FormControl('', {
      validators: [Validators.required],
    }),
    choiceProducer: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
    description: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
    imageUrl: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.urlPattern)],
      updateOn: 'change',
    }),
    price: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.pricePattern)],
      updateOn: 'change',
    }),
    choiceType: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  public updateForm = new FormGroup({
    choiceSubcategory: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
    choiceProducer: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
    choiceProduct: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
    name: new FormControl('', {
      validators: [Validators.required, Validators.minLength(3)],
      updateOn: 'change',
    }),
    description: new FormControl('', {
      validators: [
        Validators.required,
        Validators.minLength(3),
        Validators.maxLength(256),
      ],
      updateOn: 'change',
    }),
    imageUrl: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.urlPattern)],
      updateOn: 'change',
    }),
    price: new FormControl('', {
      validators: [Validators.required, Validators.pattern(this.pricePattern)],
      updateOn: 'change',
    }),
    choiceType: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  public deleteForm = new FormGroup({
    choiceProduct: new FormControl('', {
      validators: [Validators.required],
      updateOn: 'change',
    }),
  });

  constructor(
    private productService: ProductService,
    private producerService: ProducerService
  ) {
    productService.getProductTypes().subscribe((types) => {
      this.productTypes = types;
    });
    productService.getAllProducts().subscribe((products) => {
      this.products = products;
    });
    producerService.producers$.subscribe(
      (producers) => (this.producers = producers)
    );
  }

  ngOnInit(): void {}

  onSumbitAdd() {
    if (this.addForm.valid) {
      let request: ProductRequest = {
        name: this.addForm.controls['name']?.value ?? '',
        description: this.addForm.controls['description']?.value ?? '',
        imageUrl: this.addForm.controls['imageUrl']?.value ?? '',
        price: this.addForm.controls['price']?.value ?? '',
        type: this.addForm.controls['choiceType']?.value ?? '',
      };
      let subcategoryId = this.addForm.controls['choiceSubcategory']?.value;
      let producerId = this.addForm.controls['choiceProducer']?.value;
      if (subcategoryId && producerId) {
        this.productService
          .addProduct(subcategoryId, producerId, request)
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
      let request: ProductRequest = {
        name: this.updateForm.controls['name']?.value ?? '',
        description: this.updateForm.controls['description']?.value ?? '',
        imageUrl: this.updateForm.controls['imageUrl']?.value ?? '',
        price: this.updateForm.controls['price']?.value ?? '',
        type: this.updateForm.controls['choiceType']?.value ?? '',
      };
      let subcategoryId = this.updateForm.controls['choiceSubcategory']?.value;
      let producerId = this.updateForm.controls['choiceProducer']?.value;
      if (subcategoryId && producerId) {
        this.productService
          .updateProduct(subcategoryId, producerId, request)
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
      let id = this.deleteForm.controls['choiceProduct']?.value;
      if (id) {
        this.productService.deleteProduct(id).subscribe({
          next: () => window.location.reload(),
          error: (e) => {
            this.errorDeleteMsg = e.error.errors.join('<br>');
          },
        });
      }
    }
  }

  public get listProductType() {
    return this.productTypes;
  }

  public get listProducts() {
    return this.products;
  }

  public get listProducer() {
    return this.producers;
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
