import { Component, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { InterestsApi } from '../data-access/interests-api';

@Component({
  selector: 'app-interests-manager',
  standalone: true,
  templateUrl: './interests-manager.html',
  styleUrl: './interests-manager.css'
})
export class InterestsManager {
  private readonly interestsApi = inject(InterestsApi);

  protected readonly interestsResource = rxResource({
    stream: () => this.interestsApi.getAll()
  });

  protected readonly newDescription = signal('');

  protected onAdd(): void {
    const description = this.newDescription().trim();
    if (!description) {
      return;
    }

    this.interestsApi.create({ description }).subscribe({
      next: () => {
        this.newDescription.set('');
        this.interestsResource.reload();
      }
    });
  }

  protected onDelete(id: number): void {
    if (!confirm('Supprimer ce centre d\'intérêt ?')) {
      return;
    }

    this.interestsApi.delete(id).subscribe({
      next: () => this.interestsResource.reload()
    });
  }
}
