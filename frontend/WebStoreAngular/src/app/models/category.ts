//import { Subcategory } from "./subcategory";

export interface Category {
  id: string;
  name: string;
  subcategoriesDto: Subcategory[];
}

export interface Subcategory {
  id: string;
  name: string;
}
