import { HttpClient } from '@angular/common/http';
import { inject, Service } from '@angular/core';
import { Observable } from 'rxjs';

import {Project, ProjectPayload} from '../models/project';

@Service()
export class ProjectsApi {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = '/api/projects';

  getAll(): Observable<Project[]> {
    return this.http.get<Project[]>(this.apiUrl);
  }

  getById(id: number): Observable<Project> {
    return this.http.get<Project>(`${this.apiUrl}/${id}`);
  }

  create(payload: ProjectPayload): Observable<Project> {
    return this.http.post<Project>(this.apiUrl, payload);
  }

  update(id: number, payload: ProjectPayload): Observable<Project> {
    return this.http.put<Project>(
      `${this.apiUrl}/${id}`,
      payload
    );
  }

  delete(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }
}
