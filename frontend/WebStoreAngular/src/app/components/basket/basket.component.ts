import { Component, OnInit } from '@angular/core';
import { Shipment } from 'src/app/models/shipment';
import { ShopService } from 'src/app/services/shop.service';

@Component({
  selector: 'app-basket',
  templateUrl: './basket.component.html',
  styleUrls: ['./basket.component.scss'],
})
export class BasketComponent implements OnInit {
  private basket?: Shipment[];

  constructor(private shopService: ShopService) {
    this.shopService.basket$.subscribe((shipments) => {
      this.basket = shipments;
      console.log('basket: ' + this.basket);
    });
  }

  ngOnInit(): void {}

  public get shipments() {
    return this.basket;
  }
}
