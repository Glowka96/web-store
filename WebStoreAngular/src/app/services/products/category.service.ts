import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CategoryRequest } from '../../models/products/category-request';
import { Observable } from 'rxjs';
import { CategoryResponse } from '../../models/products/category-response';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(`${this.apiServerUrl}/categories`);
  }

  public addCategory(request: CategoryRequest): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/admin/categories`,
      request
    );
  }

  public updateCategory(id: string, request: CategoryRequest): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/admin/categories/${id}`,
      request
    );
  }

  public deleteCategory(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/admin/categories/${id}`);
  }
}
