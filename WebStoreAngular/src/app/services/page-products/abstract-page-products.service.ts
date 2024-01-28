import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router, ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/internal/Observable';
import { PageProductsOptions } from 'src/app/models/products/page-products-options';
import { PageProductsWithPromotion } from 'src/app/models/products/page-products-with-promotion';
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

  public abstract getPageProducts(
    options: PageProductsOptions
  ): Observable<PageProductsWithPromotion>;
}
