import { HttpClient, HttpParams } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root',
})
export abstract class AbstractPageProductsService {
  protected apiServerUrl = environment.apiBaseUrl;

  constructor(
    protected router: Router,
    protected route: ActivatedRoute,
    protected http: HttpClient
  ) {}
}
