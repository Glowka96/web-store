import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { DeliveryTypeRequest } from 'src/app/models/orders/delivery-type-request';
import { DeliveryTypeResponse } from 'src/app/models/orders/delivery-type-response';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export class DeliveryTypeService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAllDeliveryType(): Observable<DeliveryTypeResponse[]> {
    return this.http.get<DeliveryTypeResponse[]>(
      `${this.apiServerUrl}/delivery-types`
    );
  }

  public saveDeliveryType(
    request: DeliveryTypeRequest
  ): Observable<DeliveryTypeResponse> {
    return this.http.post<DeliveryTypeResponse>(
      `${this.apiServerUrl}`,
      request
    );
  }

  public deleteDeliveryType(deliveryId: number): Observable<any> {
    return this.http.delete(`${this.apiServerUrl}/${deliveryId}`);
  }
}
