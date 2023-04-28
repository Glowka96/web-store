import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ProductRequest } from '../models/product-request';
import { Observable } from 'rxjs';
import { Product } from '../models/product';
import { ActivatedRoute, Router } from '@angular/router';
import { ProductType } from '../models/product-type';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiServerUrl = environment.apiBaseUrl;
  private listProductTypes: Observable<string[]>;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.listProductTypes = this.getProductTypes();
    console.log('type' + this.listProductTypes);
  }

  public getProductsBySubcategory(
    subcategoryId: string,
    page: number = 0,
    size: number = 12
  ): Observable<Product[]> {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: page + 1, size: size },
      queryParamsHandling: 'merge',
    });
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }

  public getCountProducts(subcategoryId: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products/amount`
    );
  }

  public getSearchProducts(
    text: string,
    page: number = 0,
    size: number = 12,
    sort: string = 'id'
  ): Observable<Product[]> {
    this.router.navigate(['/search'], {
      queryParams: { q: text, page: page + 1, size: size, sort: sort },
      queryParamsHandling: 'merge',
    });
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/products/search/${text}`,
      { params }
    );
  }

  public getCountSearchProducts(text: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiServerUrl}/products/search/${text}/amount`
    );
  }

  public getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/subcategories/products`
    );
  }

  private getProductTypes(): Observable<string[]> {
    return this.http.get<string[]>(
      `${this.apiServerUrl}/subcategories/products/types`
    );
  }

  public addProduct(
    subcategoryId: string,
    producerId: string,
    request: ProductRequest
  ): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/producers/${producerId}/products`,
      request
    );
  }

  public updateProduct(
    subcategoryId: string,
    producerId: string,
    request: ProductRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/producers/${producerId}/products`,
      request
    );
  }

  public deleteProduct(productId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/subcategories/products/${productId}`
    );
  }

  public get productTypes$(): Observable<string[]> {
    return this.listProductTypes;
  }
}
