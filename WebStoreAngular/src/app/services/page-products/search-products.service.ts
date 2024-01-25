import { HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { AbstractPageProductsService } from './abstract-page-products.service';

@Injectable({
  providedIn: 'root',
})
export class SearchProductsService extends AbstractPageProductsService {
  public getPageProducts(
    text: string,
    page = 0,
    size = 12,
    sort = 'id',
    direction: 'asc'
  ): Observable<PageProductsWithPromotion> {
    this.router.navigate(['/search'], {
      queryParams: {
        text: text,
        page: page + 1,
        size: size,
        sort: sort,
        direction: direction,
      },
      queryParamsHandling: 'merge',
    });
    const params = new HttpParams()
      .set('text', text.toString())
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort.toString())
      .set('direction', direction.toString());
    return this.http.get<PageProductsWithPromotion>(
      `${this.apiServerUrl}/products/search`,
      { params }
    );
  }
}
