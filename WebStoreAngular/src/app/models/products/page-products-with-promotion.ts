import { ProductWithPromotion } from './products-with-promotion';

export interface PageProductsWithPromotion {
  totalElements: number;
  totalPages: number;
  sortByTypes: string[];
  sortDirectionsTypes: string[];
  products: ProductWithPromotion[];
}
