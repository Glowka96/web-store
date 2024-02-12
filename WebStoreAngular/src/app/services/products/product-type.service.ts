import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductTypeRequest } from 'src/app/models/products/product-type-request';
import { ProductTypResponse } from 'src/app/models/products/product-type-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductTypeService {
  private apiServerUrl = environment.apiBaseUrl;
  private productTypes: Observable<ProductTypResponse[]>;

  constructor(private http: HttpClient) {
    this.productTypes = this.getAllProductTypes();
  }

  private getAllProductTypes(): Observable<ProductTypResponse[]> {
    return this.http.get<ProductTypResponse[]>(
      `${this.apiServerUrl}/admin/product-types`
    );
  }

  public addProductType(request: ProductTypeRequest): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/admin/product-types`,
      request
    );
  }

  public updateProductType(
    productTypeId: string,
    request: ProductTypeRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/admin/product-types/${productTypeId}`,
      request
    );
  }

  public deleteProductType(productTypeId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/admin/product-types/${productTypeId}`
    );
  }

  public get productTypes$(): Observable<ProductTypResponse[]> {
    return this.productTypes;
  }
}
