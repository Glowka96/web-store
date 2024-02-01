import { Component, OnInit } from '@angular/core';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { ActivatedRoute } from '@angular/router';
import { SearchProductsService } from 'src/app/services/page-products/search-products.service';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-page-by-search-text',
  templateUrl: './page-by-search-text.component.html',
  styleUrls: ['./page-by-search-text.component.scss'],
})
export class PageBySearchTextComponent
  extends AbstractPageProductComponent
  implements OnInit
{
  private _serchedText!: string;
  protected override _pageProducts!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: SearchProductsService,
    protected override shopService: ShopService
  ) {
    super(pageService, shopService);
  }

  override ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('text')) {
        this._serchedText = params.get('text') as string;
        this.getPageProductsWithText(this._serchedText);
        this._title = 'Result of search';
      }
    });
  }
}
