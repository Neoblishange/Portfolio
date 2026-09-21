import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Education, EducationPayload } from '../models/education';

@Service()
export class EducationsApi {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/educations';

  getAll(): Observable<Education[]> {
    return this.http.get<Education[]>(this.apiUrl);
  }

  getById(id: number): Observable<Education> {
    return this.http.get<Education>(`${this.apiUrl}/${id}`);
  }

  create(payload: EducationPayload): Observable<Education> {
    return this.http.post<Education>(this.apiUrl, payload);
  }

  update(id: number, payload: EducationPayload): Observable<Education> {
    return this.http.put<Education>(`${this.apiUrl}/${id}`, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
