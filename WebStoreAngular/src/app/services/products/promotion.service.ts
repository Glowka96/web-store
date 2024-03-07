import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs/internal/Observable';
import { ProductPromotionRequest } from 'src/app/models/products/product-promotion-request';
import { ProductPromotionResponse } from 'src/app/models/products/product-promotion-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class PromotionService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public addPromotion(
    request: ProductPromotionRequest
  ): Observable<ProductPromotionResponse> {
    return this.http.post<ProductPromotionResponse>(
      `${this.apiServerUrl}/admin/products/promotions`,
      request
    );
  }

  public deletePromotion(promotionId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/admin/subcategories/products/${promotionId}`
    );
  }
}
