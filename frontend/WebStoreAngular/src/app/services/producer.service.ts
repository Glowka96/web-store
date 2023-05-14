import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { ProducerRequest } from '../models/producer-request';
import { Producer } from '../models/producer';

@Injectable({
  providedIn: 'root',
})
export class ProducerService {
  private apiServerUrl = environment.apiBaseUrl;
  private listProducer: Observable<Producer[]>;

  constructor(private http: HttpClient) {
    this.listProducer = this.getAllProducers();
  }

  private getAllProducers(): Observable<Producer[]> {
    return this.http.get<Producer[]>(`${this.apiServerUrl}/producers`);
  }

  public addProducer(request: ProducerRequest): Observable<any> {
    return this.http.post<any>(`${this.apiServerUrl}/producers`, request);
  }

  public updateProducer(
    producerId: string,
    request: ProducerRequest
  ): Observable<any> {
    return this.http.put<any>(
      `${this.apiServerUrl}/producers/${producerId}`,
      request
    );
  }

  public deleteProducer(producerId: string): Observable<any> {
    return this.http.delete<any>(
      `${this.apiServerUrl}/producers/${producerId}`
    );
  }

  public get producers$(): Observable<Producer[]> {
    return this.listProducer;
  }
}
