import { Component, OnInit } from '@angular/core';
import { SubcategoryIdProductsService } from 'src/app/services/page-products/subcategory-id-products.service';
import { ActivatedRoute } from '@angular/router';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';
import { ShopService } from 'src/app/services/olders/shop.service';
import { ProductWithPromotion } from 'src/app/models/products/products-with-promotion';

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
    console.log('in page constr');
  }

  override ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this._subcategoryId = params.get('id') as string;
        this.getPageProducts(this._subcategoryId);
        this._title = params.get('subcategoryName') as string;
        console.log('change isLoaded');
      }
    });
  }

  protected getPageProducts(
    subcategoryId: string,
    pageNo = 0,
    pageSize = 12,
    sortBy = 'id',
    sortDirection = 'asc'
  ) {
    const options = {
      text: subcategoryId,
      page: pageNo,
      size: pageSize,
      sort: sortBy,
      direction: sortDirection,
    };
    this._selectedPageNumber = pageNo;
    this.pageService.getPageProducts(options).subscribe((data) => {
      this._pageProducts = data;
      this._pageProducts.products.forEach(
        (product) => (this._cart[product.id] = 1)
      );
      window.scroll({
        top: 0,
        left: 0,
        behavior: 'smooth',
      });
    });
  }

  protected get subcategoryId() {
    return this._subcategoryId;
  }
}
