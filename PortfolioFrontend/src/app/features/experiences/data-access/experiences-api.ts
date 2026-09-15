import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Experience, ExperiencePayload } from '../models/experience';

@Service()
export class ExperiencesApi {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/experiences';

  getAll(): Observable<Experience[]> {
    return this.http.get<Experience[]>(this.apiUrl);
  }

  getById(id: number): Observable<Experience> {
    return this.http.get<Experience>(`${this.apiUrl}/${id}`);
  }

  create(payload: ExperiencePayload): Observable<Experience> {
    return this.http.post<Experience>(this.apiUrl, payload);
  }

  update(id: number, payload: ExperiencePayload): Observable<Experience> {
    return this.http.put<Experience>(`${this.apiUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
