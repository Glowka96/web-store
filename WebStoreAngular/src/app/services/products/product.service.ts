import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { ProductRequest } from '../../models/products/product-request';
import { Observable } from 'rxjs';
import { ProductResponse } from '../../models/products/product-response';
import { ProductWithProducerAndPromotion } from 'src/app/models/products/product-with-producer-and-promotion';

@Injectable({
  providedIn: 'root',
})
export class ProductService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAllProducts(): Observable<ProductResponse[]> {
    return this.http.get<ProductResponse[]>(
      `${this.apiServerUrl}/admin/products`
    );
  }

  public getProductById(
    productId: string
  ): Observable<ProductWithProducerAndPromotion> {
    return this.http.get<ProductWithProducerAndPromotion>(
      `${this.apiServerUrl}/products/${productId}`
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
