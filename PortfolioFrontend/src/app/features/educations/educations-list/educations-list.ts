import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { EducationsApi } from '../data-access/educations-api';

@Component({
  selector: 'app-educations-list',
  standalone: true,
  imports: [],
  templateUrl: './educations-list.html',
  styleUrl: './educations-list.css'
})
export class EducationsList {
  private readonly educationsApi = inject(EducationsApi);

  protected readonly educationsResource = rxResource({
    stream: () => this.educationsApi.getAll()
  });

  protected onDelete(id: number): void {
    if (!confirm('Delete this education?')) {
      return;
    }

    this.educationsApi.delete(id).subscribe({
      next: () => this.educationsResource.reload()
    });
  }
}
