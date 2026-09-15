import { Service, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Profile, ProfilePayload } from '../models/profile';

@Service()
export class ProfileApi {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = 'http://localhost:8080/api/profile';

  get(): Observable<Profile> {
    return this.http.get<Profile>(this.apiUrl);
  }

  update(payload: ProfilePayload): Observable<Profile> {
    return this.http.put<Profile>(this.apiUrl, payload);
  }
}
