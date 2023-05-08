import { Product } from './product';

export interface Shipment {
  product: Product;
  quantity: number;
  price: number;
}
