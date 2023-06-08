import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { CategoryRequest } from '../models/category-request';
import { Observable } from 'rxjs';
import { CategoryResponse } from '../models/category-response';

@Injectable({
  providedIn: 'root',
})
export class CategoryService {
  private apiServerUrl = environment.apiBaseUrl;
  private listCategory: Observable<CategoryResponse[]>;

  constructor(private http: HttpClient) {
    this.listCategory = this.getCategories();
  }

  private getCategories(): Observable<CategoryResponse[]> {
    return this.http.get<CategoryResponse[]>(`${this.apiServerUrl}/categories`);
  }

  public get categories$(): Observable<CategoryResponse[]> {
    return this.listCategory;
  }

  public addCategory(request: CategoryRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/categories`, request);
  }

  public updateCategory(id: string, request: CategoryRequest): Observable<any> {
    return this.http.put<any>(`${this.apiServerUrl}/categories/${id}`, request);
  }

  public deleteCategory(id: string): Observable<any> {
    return this.http.delete<any>(`${this.apiServerUrl}/categories/${id}`);
  }
}
