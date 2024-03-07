import { DeliveryResponse } from './delivery-response';
import { ShipmentResponse } from './shipment-response';

export interface OrderResponse {
  id: string;
  nameUser: string;
  totalPrice: number;
  delivery: DeliveryResponse;
  dateOfCreation: Date;
  status: string;
  shipments: ShipmentResponse[];
}
