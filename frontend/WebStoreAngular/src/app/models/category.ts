import { Subcategory } from './subcategory';

export interface Category {
  id: string;
  name: string;
  subcategoriesDto: Subcategory[];
}
