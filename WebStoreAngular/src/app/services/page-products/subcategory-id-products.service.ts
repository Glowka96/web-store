import { Injectable } from '@angular/core';
import { AbstractPageProductsService } from './abstract-page-products.service';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageProductsOptions } from 'src/app/models/products/page-products-options';

@Injectable({
  providedIn: 'root',
})
export class SubcategoryIdProductsService extends AbstractPageProductsService {
  public override getPageProducts(
    options: PageProductsOptions
  ): Observable<PageProductsWithPromotion> {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: options.page + 1,
        size: options.size,
        sort: options.sort,
        direction: options.direction,
      },
      queryParamsHandling: 'merge',
    });
    const params = new HttpParams()
      .set('page', options.page)
      .set('size', options.size)
      .set('sort', options.sort)
      .set('direction', options.direction);
    return this.http.get<PageProductsWithPromotion>(
      `${this.apiServerUrl}/subcategories/${options.text}/products`,
      { params }
    );
  }
}
