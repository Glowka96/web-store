import { Product } from './product';

export interface Shipment {
  product: Product;
  quality: number;
  price: number;
}
