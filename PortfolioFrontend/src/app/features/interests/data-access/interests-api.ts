import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Interest, InterestPayload } from '../models/interest';

@Service()
export class InterestsApi {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/interests';

  getAll(): Observable<Interest[]> {
    return this.http.get<Interest[]>(this.apiUrl);
  }

  create(payload: InterestPayload): Observable<Interest> {
    return this.http.post<Interest>(this.apiUrl, payload);
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
