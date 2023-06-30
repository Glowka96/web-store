import { ShipmentResponse } from './shipment-response';

export interface OrderResponse {
  id: string;
  nameUser: string;
  productsPrice: number;
  deliveryAddress: string;
  shipmentAddress: string;
  dateOfCreated: Date;
  status: string;
  shipments: ShipmentResponse[];
}
