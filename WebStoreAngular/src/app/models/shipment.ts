import { ProductWithPromotion } from './products/products-with-promotion';

export interface Shipment {
  product: ProductWithPromotion;
  quantity: number;
}
