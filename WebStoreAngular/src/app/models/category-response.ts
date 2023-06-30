import { SubcategoryResponse } from './subcategory-response';

export interface CategoryResponse {
  id: string;
  name: string;
  subcategories: SubcategoryResponse[];
}
