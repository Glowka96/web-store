import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Category } from './model/category';


@Injectable({
  providedIn: 'root'
})
export class CategoryService {
  private apiServerUrl = environment.apiBaseUrl;
  private listCategory: Observable<Category[]>;

  constructor(private http: HttpClient) {
    this.listCategory = this.getCategories();
   }

  private getCategories(): Observable<Category[]>{
    console.log('start')
    return this.http.get<Category[]>(`${this.apiServerUrl}/categories`);
  }

  public get categories(): Observable<Category[]>{
     return this.listCategory;
   }
}
