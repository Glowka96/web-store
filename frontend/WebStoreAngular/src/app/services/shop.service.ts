import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient, HttpParams } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class ShopService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}
}
