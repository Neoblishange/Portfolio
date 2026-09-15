import { Component, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { CategoriesApi } from '../data-access/categories-api';

@Component({
  selector: 'app-categories-manager',
  standalone: true,
  templateUrl: './categories-manager.html',
  styleUrl: './categories-manager.css'
})
export class CategoriesManager {
  private readonly categoriesApi = inject(CategoriesApi);

  protected readonly categoriesResource = rxResource({
    stream: () => this.categoriesApi.getAll()
  });

  protected readonly newCategoryName = signal('');

  protected onAdd(): void {
    const name = this.newCategoryName().trim();
    if (!name) {
      return;
    }

    this.categoriesApi.create({ name }).subscribe({
      next: () => {
        this.newCategoryName.set('');
        this.categoriesResource.reload();
      }
    });
  }

  protected onDelete(id: number): void {
    if (!confirm('Supprimer cette catégorie ? Les skills associés seront aussi supprimés.')) {
      return;
    }

    this.categoriesApi.delete(id).subscribe({
      next: () => this.categoriesResource.reload()
    });
  }
}
