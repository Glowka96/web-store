import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { BehaviorSubject, Observable } from 'rxjs';
import { Category } from './models/category';
import { Product } from './models/product';

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

  getProductsBySubcategory(
    subcategoryId: string,
    page: number = 0,
    size: number = 10
  ): Observable<Product[]> {
    console.log('gets products');
    const params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }
}
