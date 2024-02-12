import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { ProductTypeRequest } from 'src/app/models/products/product-type-request';
import { ProductTypeResponse } from 'src/app/models/products/product-type-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class ProductTypeService {
  private apiServerUrl = environment.apiBaseUrl;
  private productTypes: Observable<ProductTypeResponse[]>;

  constructor(private http: HttpClient) {
    this.productTypes = this.getAllProductTypes();
  }

  private getAllProductTypes(): Observable<ProductTypeResponse[]> {
    return this.http.get<ProductTypeResponse[]>(
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

  public get productTypes$(): Observable<ProductTypeResponse[]> {
    return this.productTypes;
  }
}
