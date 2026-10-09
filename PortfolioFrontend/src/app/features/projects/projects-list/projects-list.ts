import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { ProjectsApi } from '../data-access/projects-api';

@Component({
  selector: 'app-projects-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './projects-list.html',
  styleUrl: './projects-list.css'
})
export class ProjectsList {
  private readonly projectsApi = inject(ProjectsApi);

  protected readonly projectsResource = rxResource({
    stream: () => this.projectsApi.getAll()
  });

  /*
  protected onDelete(id: number): void {
    if (!confirm('Delete this project?')) {
      return;
    }

    this.projectsApi.delete(id).subscribe({
      next: () => this.projectsResource.reload()
    });
  }
  */
}
