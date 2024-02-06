import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject, Observable, tap } from 'rxjs';
import { Shipment } from '../../models/shipment';
import { OrderRequest } from '../../models/order-request';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private basket: BehaviorSubject<Shipment[]> = new BehaviorSubject(
    [] as Shipment[]
  );
  constructor(private http: HttpClient) {
    const localStorageData = localStorage.getItem('basket');
    let localStorageBasket: Shipment[] = [];
    if (localStorageData) {
      localStorageBasket = JSON.parse(localStorageData);
      this.basket = new BehaviorSubject(localStorageBasket);
    }
  }

  public addToBasket(shipment: Shipment) {
    const cart = this.basket.value;
    const findShipment = cart.find((s) => s.product.id == shipment.product.id);
    if (findShipment) {
      findShipment.quantity += shipment.quantity;
    } else {
      cart.push(shipment);
    }
    console.log(cart);
    this.basket.next(cart);
    localStorage.setItem('basket', JSON.stringify(cart));
  }

  public purchase(request: OrderRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/accounts/orders`, request);
  }

  public get basket$() {
    return this.basket;
  }
}
