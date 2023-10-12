import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ProductRequest } from '../models/product-request';
import { Observable } from 'rxjs';
import { ProductResponse } from '../models/product-response';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {}

  public getProductsBySubcategory(
    subcategoryId: string,
    page = 0,
    size = 12,
    sort = 'id'
  ): Observable<ProductResponse[]> {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: page + 1, size: size, sort: sort },
      queryParamsHandling: 'merge',
    });
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort.toString());
    return this.http.get<ProductResponse[]>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }

  public getQuantityOfProducts(subcategoryId: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products/quantity`
    );
  }

  public getSearchProducts(
    text: string,
    page = 0,
    size = 12,
    sort = 'id'
  ): Observable<ProductResponse[]> {
    this.router.navigate(['/search'], {
      queryParams: { text: text, page: page + 1, size: size, sort: sort },
      queryParamsHandling: 'merge',
    });
    const params = new HttpParams()
      .set('text', text.toString())
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<ProductResponse[]>(
      `${this.apiServerUrl}/products/search`,
      { params }
    );
  }

  public getQuanititySearchProducts(text: string): Observable<number> {
    const params = new HttpParams().set('text', text.toString());
    return this.http.get<number>(
      `${this.apiServerUrl}/products/search/quantity`,
      { params }
    );
  }

  public getAllProducts(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(
      `${this.apiServerUrl}/admin/subcategories/products`
    );
  }

  public getProductTypes(): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.apiServerUrl}/admin/subcategories/products/types`
    );
  }

  public addProduct(
    subcategoryId: string,
    producerId: string,
    request: ProductRequest
  ): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/admin/subcategories/${subcategoryId}/producers/${producerId}/products`,
      request
    );
  }

  public updateProduct(
    subcategoryId: string,
    producerId: string,
    request: ProductRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/admin/subcategories/${subcategoryId}/producers/${producerId}/products`,
      request
    );
  }

  public deleteProduct(productId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/admin/subcategories/products/${productId}`
    );
  }
}
