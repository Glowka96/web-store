import { Component, OnInit } from '@angular/core';
import { SubcategoryIdProductsService } from 'src/app/services/page-products/subcategory-id-products.service';
import { ActivatedRoute } from '@angular/router';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { ShopService } from 'src/app/services/olders/shop.service';

@Component({
  selector: 'app-page-products',
  templateUrl: './page-by-subcategory-id.component.html',
  styleUrls: ['./page-by-subcategory-id.component.scss'],
})
export class PageBySubcategoryId
  extends AbstractPageProductComponent
  implements OnInit
{
  private _subcategoryId!: string;
  protected override _pageProducts!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: SubcategoryIdProductsService,
    protected override shopService: ShopService
  ) {
    super(pageService, shopService);
  }

  override ngOnInit(): void {
    this.routeSubscription = this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this._subcategoryId = params.get('id') as string;
        this.getPageProductsWithText(this._subcategoryId);
        this._title = params.get('subcategoryName') as string;
      }
    });
  }

  protected get subcategoryId() {
    return this._subcategoryId;
  }
}
