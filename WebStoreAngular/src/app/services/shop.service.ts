import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { ShipmentRequest } from '../models/shipment-request';
import { OrderRequest } from '../models/order-request';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private basket: BehaviorSubject<ShipmentRequest[]> = new BehaviorSubject(
    [] as ShipmentRequest[]
  );
  constructor(private http: HttpClient) {}

  public addToBasket(shipment: ShipmentRequest) {
    const cart = this.basket.value;
    const findShipment = cart.find((s) => s.product.id == shipment.product.id);
    if (findShipment) {
      findShipment.price = (
        Number(findShipment.price) + Number(shipment.price)
      ).toFixed(2);
      findShipment.quantity += shipment.quantity;
    } else {
      cart.push(shipment);
    }
    this.basket.next(cart);
  }

  public purchase(accountId: string, request: OrderRequest): Observable<any> {
    return this.http
      .post<any>(`${this.apiServerUrl}/accounts/${accountId}/orders`, request)
      .pipe(tap((response) => console.log('Server response:', response)));
  }

  public get basket$() {
    return this.basket;
  }
}
