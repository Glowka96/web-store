import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { OrderResponse } from '../../models/orders/order-response';
import { Observable } from 'rxjs';
import { OrderRequest } from '../../models/orders/order-request';

@Injectable({
  providedIn: 'root',
})
export class OrdersService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAllAccountOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(
      `${this.apiServerUrl}/accounts/orders`
    );
  }

  public deleteOrder(orderId: string): Observable<any> {
    return this.http.delete(`${this.apiServerUrl}/accounts/orders/${orderId}`);
  }
}
