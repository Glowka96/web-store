import { Injectable } from '@angular/core';
import { AbstractPageProductsService } from './abstract-page-products.service';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SubcategoryIdProductsService extends AbstractPageProductsService {
  public getProductsBySubcategory(
    subcategoryId: string,
    page = 0,
    size = 12,
    sort = 'id',
    direction = 'asc'
  ): Observable<PageProductsWithPromotion> {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: {
        page: page + 1,
        size: size,
        sort: sort,
        direction: direction,
      },
      queryParamsHandling: 'merge',
    });
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort.toString())
      .set('direction', direction.toString());
    return this.http.get<PageProductsWithPromotion>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }
}
