import { Component, OnInit } from '@angular/core';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { ActivatedRoute, Params } from '@angular/router';
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
  protected _searchedText!: string;
  protected override _pageProducts!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: SearchProductsService,
    protected override shopService: ShopService
  ) {
    super(pageService, shopService);
  }

  override ngOnInit(): void {
    this.route.queryParams.subscribe((params: Params) => {
      if (params['query']) {
        this._searchedText = params['query'];
        this.getPageProductsWithText(this._searchedText);
        this._title = 'Result of search';
      }
    });
  }
}
