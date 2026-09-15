import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { ExperiencesApi } from '../data-access/experiences-api';

@Component({
  selector: 'app-experiences-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './experiences-list.html',
  styleUrl: './experiences-list.css'
})
export class ExperiencesList {
  private readonly experiencesApi = inject(ExperiencesApi);

  protected readonly experiencesResource = rxResource({
    stream: () => this.experiencesApi.getAll()
  });

  protected onDelete(id: number): void {
    if (!confirm('Supprimer cette expérience ?')) {
      return;
    }

    this.experiencesApi.delete(id).subscribe({
      next: () => this.experiencesResource.reload()
    });
  }
}
