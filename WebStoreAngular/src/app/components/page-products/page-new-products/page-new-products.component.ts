import { Component, OnInit } from '@angular/core';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { NewProductsService } from 'src/app/services/page-products/new-products.service';
import { ActivatedRoute } from '@angular/router';
import { ShopService } from 'src/app/services/olders/shop.service';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';

@Component({
  selector: 'app-page-new-products',
  templateUrl: './page-new-products.component.html',
  styleUrls: ['./page-new-products.component.scss'],
})
export class PageNewProductsComponent
  extends AbstractPageProductComponent
  implements OnInit
{
  protected override _pageProducts!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: NewProductsService,
    protected override shopService: ShopService
  ) {
    super(pageService, shopService);
  }

  override ngOnInit(): void {
    this._routeSubscription = this.route.paramMap.subscribe(() => {
      this.getPageProducts();
      this._title = 'New products';
    });
  }
}
