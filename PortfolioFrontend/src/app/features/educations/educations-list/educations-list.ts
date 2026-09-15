import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { EducationsApi } from '../data-access/educations-api';

@Component({
  selector: 'app-educations-list',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './educations-list.html',
  styleUrl: './educations-list.css'
})
export class EducationsList {
  private readonly educationsApi = inject(EducationsApi);

  protected readonly educationsResource = rxResource({
    stream: () => this.educationsApi.getAll()
  });

  protected onDelete(id: number): void {
    if (!confirm('Supprimer cette formation ?')) {
      return;
    }

    this.educationsApi.delete(id).subscribe({
      next: () => this.educationsResource.reload()
    });
  }
}
