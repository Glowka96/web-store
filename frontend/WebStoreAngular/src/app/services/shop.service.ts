import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { Product } from '../models/product';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private listCategory: Observable<Category[]>;

  constructor(private http: HttpClient) {
    this.listCategory = this.getCategories();
  }

  private getCategories(): Observable<Category[]> {
    console.log('start');
    return this.http.get<Category[]>(`${this.apiServerUrl}/categories`);
  }

  public get categories(): Observable<Category[]> {
    return this.listCategory;
  }

  public getProductsBySubcategory(
    subcategoryId: string,
    page: number = 0,
    size: number = 12
  ): Observable<Product[]> {
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }
}