import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Shipment } from '../models/shipment';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private basket: BehaviorSubject<Shipment[]> = new BehaviorSubject(
    [] as Shipment[]
  );
  constructor(private http: HttpClient) {}

  addToBasket(shipment: Shipment) {
    let cart = this.basket.value;
    let findShipment = cart.find((s) => s.product.id == shipment.product.id);
    if (findShipment) {
      findShipment.price = findShipment.price + shipment.price;
      findShipment.quantity += shipment.quantity;
    } else {
      cart.push(shipment);
    }
    this.basket.next(cart);
  }

  public get basket$() {
    return this.basket;
  }
}
