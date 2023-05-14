import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Shipment } from '../models/shipment';
import { Order } from '../models/order';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private basket: BehaviorSubject<Shipment[]> = new BehaviorSubject(
    [] as Shipment[]
  );
  constructor(private http: HttpClient) {}

  public addToBasket(shipment: Shipment) {
    let cart = this.basket.value;
    let findShipment = cart.find((s) => s.product.id == shipment.product.id);
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

  public purchase(request: Order, accountId: string): Observable<any> {
    return this.http
      .post<any>(`${this.apiServerUrl}/accounts/${accountId}/orders`, request)
      .pipe(tap((response) => console.log('Server response:', response)));
  }

  public get basket$() {
    return this.basket;
  }
}
