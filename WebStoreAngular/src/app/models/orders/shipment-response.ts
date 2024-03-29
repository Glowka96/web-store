import { ProductResponse } from '../products/product-response';

export interface ShipmentResponse {
  id: string;
  product: ProductResponse;
  quantity: string;
  price: string;
}
