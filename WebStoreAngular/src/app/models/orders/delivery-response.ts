import { DeliveryTypeResponse } from './delivery-type-response';

export interface DeliveryResponse {
  id: number;
  deliveryAddress: string;
  shipmentAddress: string;
  deliveryType: DeliveryTypeResponse;
}
