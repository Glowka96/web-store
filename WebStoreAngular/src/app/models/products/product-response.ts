import { ProductTypeResponse } from './product-type-response';

export interface ProductResponse {
  id: string;
  name: string;
  description: string;
  imageUrl: string;
  price: string;
  type: ProductTypeResponse;
}
