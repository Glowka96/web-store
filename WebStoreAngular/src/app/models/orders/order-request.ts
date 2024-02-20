import { DeliveryRequest } from './delivery-request';
import { ShipmentRequest } from './shipment-request';

export interface OrderRequest {
  shipments: ShipmentRequest[];
  delivery: DeliveryRequest;
}
