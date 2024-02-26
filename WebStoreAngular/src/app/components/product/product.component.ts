import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription, take } from 'rxjs';
import { ProductWithProducerAndPromotion } from 'src/app/models/products/product-with-producer-and-promotion';
import { ProductService } from 'src/app/services/products/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit {
  private _product!: ProductWithProducerAndPromotion;
  private _routeSubscription!: Subscription;

  constructor(
    private productService: ProductService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this._routeSubscription = this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        const productId = params.get('id') as string;
        this.productService
          .getProductById(productId)
          .pipe(take(1))
          .subscribe((product) => {
            this._product = product;
          });
      }
    });
  }

  ngOnDestroy() {
    this._routeSubscription.unsubscribe();
  }

  public get product() {
    return this._product;
  }
}
