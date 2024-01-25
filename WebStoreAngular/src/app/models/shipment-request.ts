import { ProductResponse } from './products/product-response';

export interface ShipmentRequest {
  product: ProductResponse;
  quantity: number;
  price: string;
}
