import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Shipment } from '../../models/orders/shipment';
import { OrderRequest } from '../../models/orders/order-request';
import { OrderService } from './order.service';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private basket: BehaviorSubject<Shipment[]> = new BehaviorSubject(
    [] as Shipment[]
  );
  constructor(private orderService: OrderService) {
    const localStorageData = localStorage.getItem('basket');
    let localStorageBasket: Shipment[] = [];
    if (localStorageData) {
      localStorageBasket = JSON.parse(localStorageData);
      this.basket = new BehaviorSubject(localStorageBasket);
    }
  }

  public saveBasket(basket: Shipment[]) {
    this.basket.next(basket);
    localStorage.setItem('basket', JSON.stringify(basket));
  }

  public addToBasket(shipment: Shipment) {
    const cart = this.basket.value;
    const findShipment = cart.find((s) => s.product.id == shipment.product.id);
    if (findShipment) {
      findShipment.quantity += shipment.quantity;
    } else {
      cart.push(shipment);
    }
    this.basket.next(cart);
    localStorage.setItem('basket', JSON.stringify(cart));
  }

  public purchase(request: OrderRequest): Observable<any> {
    return this.orderService.saveOrder(request);
  }

  public get basket$() {
    return this.basket;
  }
}
