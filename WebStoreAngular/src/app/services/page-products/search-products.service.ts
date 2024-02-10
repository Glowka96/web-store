import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { AbstractPageProductsService } from './abstract-page-products.service';
import { PageProductsOptions } from 'src/app/models/products/page-products-options';

@Injectable({
  providedIn: 'root',
})
export class SearchProductsService extends AbstractPageProductsService {
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
      .set('query', options.text || '')
      .set('page', options.page)
      .set('size', options.size)
      .set('sort', options.sort)
      .set('direction', options.direction);
    return this.http.get<PageProductsWithPromotion>(
      `${this.apiServerUrl}/products/search`,
      { params }
    );
  }
}
