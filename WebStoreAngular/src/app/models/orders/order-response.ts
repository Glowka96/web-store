import { DeliveryResponse } from './delivery-response';
import { ShipmentResponse } from './shipment-response';

export interface OrderResponse {
  id: string;
  nameUser: string;
  productsPrice: number;
  delivery: DeliveryResponse;
  shipmentAddress: string;
  dateOfCreated: Date;
  status: string;
  shipments: ShipmentResponse[];
}
