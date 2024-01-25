import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { SubcategoryIdProductsService } from 'src/app/services/page-products/subcategory-id-products.service';
import { ActivatedRoute } from '@angular/router';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { AbstractPageProductComponent } from '../abstract-page-product/abstract-page-product.component';

@Component({
  selector: 'app-page-products',
  templateUrl: './page-products.component.html',
  styleUrls: ['./page-products.component.scss'],
})
export class PageProductsComponent
  extends AbstractPageProductComponent
  implements OnInit
{
  private subcategoryId!: string;
  protected override pageProduct!: PageProductsWithPromotion;

  constructor(
    private route: ActivatedRoute,
    private pageService: SubcategoryIdProductsService
  ) {
    super(pageService);
    console.log('in page constr');
  }

  override ngOnInit(): void {
    this.route.paramMap.subscribe((params) => {
      if (params.get('id')) {
        this.subcategoryId = params.get('id') as string;
        this.getPageProducts(this.subcategoryId);
        this.pageTitle = params.get('subcategoryName') as string;
        console.log('change isLoaded');
      }
    });
  }

  private getPageProducts(subcategoryId: string) {
    this.pageService
      .getProductsBySubcategory(subcategoryId)
      .subscribe((data) => {
        this.pageProduct = data;
        console.log('in get products page');
        console.log(data);
        window.scroll({
          top: 0,
          left: 0,
          behavior: 'smooth',
        });
      });
  }
}
