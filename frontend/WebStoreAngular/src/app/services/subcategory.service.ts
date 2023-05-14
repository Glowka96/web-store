import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { SubcategoryRequest } from '../models/subcategory-request';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { Subcategory } from '../models/subcategory';

@Injectable({
  providedIn: 'root',
})
export class SubcategoryService {
  private apiServerUrl = environment.apiBaseUrl;
  private listSubcategory: Observable<Subcategory[]>;

  constructor(private http: HttpClient) {
    this.listSubcategory = this.getSubcategories();
  }

  private getSubcategories(): Observable<Subcategory[]> {
    return this.http.get<Subcategory[]>(
      `${this.apiServerUrl}/categories/subcategories`
    );
  }

  public get subcategories$(): Observable<Subcategory[]> {
    return this.listSubcategory;
  }

  public addSubcategory(
    categoryId: string,
    request: SubcategoryRequest
  ): Observable<any> {
    return this.http.post<any>(
      `${this.apiServerUrl}/categories/${categoryId}/subcategories`,
      request
    );
  }

  public updateSubcategory(
    categoryId: string,
    subcategoryId: string,
    request: SubcategoryRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/categories/${categoryId}/subcategories/${subcategoryId}`,
      request
    );
  }

  public deleteSubcategory(subcategoryId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/categories/subcategories/${subcategoryId}`
    );
  }
}
