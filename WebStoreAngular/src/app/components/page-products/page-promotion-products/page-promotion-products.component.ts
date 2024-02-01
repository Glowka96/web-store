import { Component, OnInit } from '@angular/core';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { ActivatedRoute } from '@angular/router';
import { PromotionProductsService } from 'src/app/services/page-products/promotion-products.service';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-page-promotions-products',
  templateUrl: './page-promotion-products.component.html',
  styleUrls: ['./page-promotion-products.component.scss'],
})
export class PagePromotionsProductsComponent
  extends AbstractPageProductComponent
  implements OnInit
{
  protected override _pageProducts!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: PromotionProductsService,
    protected override shopService: ShopService
  ) {
    super(pageService, shopService);
  }

  override ngOnInit(): void {
    this.route.paramMap.subscribe(() => {
      this.getPageProducts();
      this._title = 'Promotions product';
    });
  }
}
