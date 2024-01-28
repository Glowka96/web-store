import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProducerRequest } from '../../models/producer-request';
import { ProducerResponse } from '../../models/producer-response';

@Injectable({
  providedIn: 'root',
})
export class ProducerService {
  private apiServerUrl = environment.apiBaseUrl;
  private listProducer: Observable<ProducerResponse[]>;

  constructor(private http: HttpClient) {
    this.listProducer = this.getAllProducers();
  }

  private getAllProducers(): Observable<ProducerResponse[]> {
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

  public get producers$(): Observable<ProducerResponse[]> {
    return this.listProducer;
  }
}
