import { Shipment } from './shipment';

export interface Order {
  shipments: Shipment[];
  deliveryAddress: string;
}
