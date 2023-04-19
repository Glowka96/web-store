import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from '../models/category';
import { Product } from '../models/product';
import { ActivatedRoute, Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;
  private listCategory: Observable<Category[]>;

  constructor(
    private http: HttpClient,
    private router: Router,
    private route: ActivatedRoute
  ) {
    this.listCategory = this.getCategories();
  }

  private getCategories(): Observable<Category[]> {
    return this.http.get<Category[]>(`${this.apiServerUrl}/categories`);
  }

  public get categories$(): Observable<Category[]> {
    return this.listCategory;
  }

  public getProductsBySubcategory(
    subcategoryId: string,
    page: number = 0,
    size: number = 12
  ): Observable<Product[]> {
    this.router.navigate([], {
      relativeTo: this.route,
      queryParams: { page: page + 1, size: size },
      queryParamsHandling: 'merge',
    });
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString());
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products`,
      { params }
    );
  }

  public getCountProducts(subcategoryId: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiServerUrl}/subcategories/${subcategoryId}/products/amount`
    );
  }

  public getSearchProducts(
    text: string,
    page: number = 0,
    size: number = 12,
    sort: string = 'id'
  ): Observable<Product[]> {
    this.router.navigate(['/search'], {
      queryParams: { q: text, page: page + 1, size: size, sort: sort },
      queryParamsHandling: 'merge',
    });
    console.log('page number ' + page);
    let params = new HttpParams()
      .set('page', page.toString())
      .set('size', size.toString())
      .set('sort', sort);
    return this.http.get<Product[]>(
      `${this.apiServerUrl}/products/search/${text}`,
      { params }
    );
  }

  public getCountSearchProducts(text: string): Observable<number> {
    return this.http.get<number>(
      `${this.apiServerUrl}/products/search/${text}/amount`
    );
  }
}
