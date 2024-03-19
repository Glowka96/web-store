import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { OrderResponse } from '../../models/orders/order-response';
import { Observable } from 'rxjs';
import { OrderRequest } from '../../models/orders/order-request';

@Injectable({
  providedIn: 'root',
})
export class OrderService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAllAccountOrders(): Observable<OrderResponse[]> {
    return this.http.get<OrderResponse[]>(
      `${this.apiServerUrl}/accounts/orders`
    );
  }

  public getLastFiveAccountOrders() {
    return this.http.get<OrderResponse[]>(
      `${this.apiServerUrl}/accounts/orders/last-five`
    );
  }

  public getOrderById(orderId: string) {
    return this.http.get<OrderResponse>(
      `${this.apiServerUrl}/accounts/orders/${orderId}`
    );
  }

  public saveOrder(request: OrderRequest): Observable<any> {
    console.log(JSON.stringify(request));
    return this.http.post<any>(`${this.apiServerUrl}/accounts/orders`, request);
  }
}
