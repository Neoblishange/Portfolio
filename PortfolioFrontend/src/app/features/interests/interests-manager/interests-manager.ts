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

  protected readonly newName = signal('');
  protected readonly newDescription = signal('');

  protected onAdd(): void {
    const name = this.newName().trim();
    const description = this.newDescription().trim();
    if (!name || !description) {
      return;
    }

    this.interestsApi.create({ name, description }).subscribe({
      next: () => {
        this.newName.set('');
        this.newDescription.set('');
        this.interestsResource.reload();
      }
    });
  }

  protected onDelete(id: number): void {
    if (!confirm('Delete this interest?')) {
      return;
    }

    this.interestsApi.delete(id).subscribe({
      next: () => this.interestsResource.reload()
    });
  }
}
