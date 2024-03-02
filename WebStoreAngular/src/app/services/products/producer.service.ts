import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProducerRequest } from '../../models/products/producer-request';
import { ProducerResponse } from '../../models/products/producer-response';

@Injectable({
  providedIn: 'root',
})
export class ProducerService {
  private apiServerUrl = environment.apiBaseUrl;

  constructor(private http: HttpClient) {}

  public getAllProducers(): Observable<ProducerResponse[]> {
    return this.http.get<ProducerResponse[]>(
      `${this.apiServerUrl}/admin/producers`
    );
  }

  public addProducer(request: ProducerRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/admin/producers`, request);
  }

  public updateProducer(
    producerId: string,
    request: ProducerRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/admin/producers/${producerId}`,
      request
    );
  }

  public deleteProducer(producerId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/admin/producers/${producerId}`
    );
  }
}
