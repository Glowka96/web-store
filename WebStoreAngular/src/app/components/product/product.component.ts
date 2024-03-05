import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { take } from 'rxjs';
import { Shipment } from 'src/app/models/orders/shipment';
import { ProductWithProducerAndPromotion } from 'src/app/models/products/product-with-producer-and-promotion';
import { ProductWithPromotion } from 'src/app/models/products/products-with-promotion';
import { ShopService } from 'src/app/services/olders/shop.service';
import { ProductService } from 'src/app/services/products/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.scss'],
})
export class ProductComponent implements OnInit {
  private _product!: ProductWithProducerAndPromotion;

  constructor(
    private productService: ProductService,
    private shopService: ShopService,
    private route: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
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

  public addToBasket() {
    const product = this.mapToProductWithPromotion(this._product);
    console.log(JSON.stringify(product));
    const shipment: Shipment = { product: product, quantity: 1 };
    this.shopService.addToBasket(shipment);
  }

  private mapToProductWithPromotion(
    productWithProducerAndPromotion: ProductWithProducerAndPromotion
  ): ProductWithPromotion {
    const {
      id,
      name,
      imageUrl,
      quantity,
      price,
      promotionPrice,
      lowestPrice,
      endDate,
    } = productWithProducerAndPromotion;
    return {
      id,
      name,
      imageUrl,
      quantity,
      price,
      promotionPrice,
      lowestPrice,
      endDate,
    };
  }

  public isPromotions(product: ProductWithPromotion): boolean {
    return product.promotionPrice ? true : false;
  }

  public get product() {
    return this._product;
  }
}
