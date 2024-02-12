import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubcategoryRequest } from '../../models/products/subcategory-request';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { SubcategoryResponse } from '../../models/products/subcategory-response';

@Injectable({
  providedIn: 'root',
})
export class SubcategoryService {
  private apiServerUrl = environment.apiBaseUrl;
  private listSubcategory: Observable<SubcategoryResponse[]>;

  constructor(private http: HttpClient) {
    this.listSubcategory = this.getSubcategories();
  }

  private getSubcategories(): Observable<SubcategoryResponse[]> {
    return this.http.get<SubcategoryResponse[]>(
      `${this.apiServerUrl}/categories/subcategories`
    );
  }

  public get subcategories$(): Observable<SubcategoryResponse[]> {
    return this.listSubcategory;
  }

  public addSubcategory(
    categoryId: string,
    request: SubcategoryRequest
  ): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/admin/categories/${categoryId}/subcategories`,
      request
    );
  }

  public updateSubcategory(
    categoryId: string,
    subcategoryId: string,
    request: SubcategoryRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/admin/categories/${categoryId}/subcategories/${subcategoryId}`,
      request
    );
  }

  public deleteSubcategory(subcategoryId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/admin/categories/subcategories/${subcategoryId}`
    );
  }
}
