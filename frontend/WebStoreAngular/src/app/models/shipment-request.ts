import { ProductResponse } from './product-response';

export interface ShipmentRequest {
  product: ProductResponse;
  quantity: number;
  price: string;
}
