import { Component, inject } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { ActivatedRoute, RouterLink } from '@angular/router';
import { map } from 'rxjs';
import { ProjectsApi } from '../data-access/projects-api';

@Component({
  selector: 'app-projects-detail',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './projects-detail.html',
  styleUrl: './projects-detail.css'
})
export class ProjectsDetail {
  private readonly route = inject(ActivatedRoute);
  private readonly projectsApi = inject(ProjectsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => Number(params.get('id'))))
  );

  protected readonly projectResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => this.projectsApi.getById(id)
  });
}
