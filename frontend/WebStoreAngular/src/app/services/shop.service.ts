import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { BehaviorSubject } from 'rxjs';
import { Product } from '../models/product';
import { Shipment } from '../models/shipment';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  public basket: BehaviorSubject<Shipment[]> = new BehaviorSubject(
    [] as Shipment[]
  );
  constructor(private http: HttpClient) {}

  addToBasket(shipment: Shipment) {
    console.log('start methods');
    let cart = this.basket.value;
    console.log(cart);
    let findShipment = cart.find((s) => s.product.id == shipment.product.id);
    if (findShipment) {
      findShipment.price = findShipment.price + shipment.price;
      findShipment.quality += shipment.quality;
    } else {
      cart.push(shipment);
    }
    this.basket.next(cart);
    console.log(cart);
  }
}
