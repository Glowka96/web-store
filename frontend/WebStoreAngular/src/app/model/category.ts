//import { Subcategory } from "./subcategory";

export interface Category{
  name: string,
  subcategoriesDto: Subcategory[];
}

export interface Subcategory{
  name: string;
}

