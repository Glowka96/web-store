import { ProductWithPromotion } from './products/products-with-promotion';

export interface ShipmentRequest {
  product: ProductWithPromotion;
  quantity: number;
}
