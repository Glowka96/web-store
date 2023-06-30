import { ShipmentRequest } from './shipment-request';

export interface OrderRequest {
  shipments: ShipmentRequest[];
  deliveryAddress: string;
}
