import { Component, computed, effect, inject, signal } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { form, required, minLength, maxLength, submit, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, of } from 'rxjs';
import { ProjectsApi } from '../data-access/projects-api';
import { ProjectPayload } from '../models/project';

const EMPTY_PROJECT: ProjectPayload = {
  title: '',
  slug: '',
  shortDescription: '',
  description: '',
  startDate: '',
  endDate: ''
};

function slugify(value: string): string {
  return value
    .toLowerCase()
    .normalize('NFD')
    .replace(/[\u0300-\u036f]/g, '')
    .replace(/[^a-z0-9]+/g, '-')
    .replace(/(^-|-$)/g, '');
}

@Component({
  selector: 'app-projects-form',
  standalone: true,
  imports: [FormField],
  templateUrl: './projects-form.html',
  styleUrl: './projects-form.css'
})
export class ProjectsForm {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly projectsApi = inject(ProjectsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(
      map(params => (params.get('id') ? Number(params.get('id')) : null))
    )
  );

  protected readonly isEditMode = computed(() => this.id() !== null);

  private readonly existingProjectResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => (id ? this.projectsApi.getById(id) : of(undefined))
  });

  protected readonly model = signal<ProjectPayload>({ ...EMPTY_PROJECT });

  private readonly slugManuallyEdited = signal(false);

  constructor() {
    effect(() => {
      const project = this.existingProjectResource.value();
      if (project) {
        this.slugManuallyEdited.set(true);
        this.model.set({
          title: project.title,
          slug: project.slug,
          shortDescription: project.shortDescription,
          description: project.description,
          startDate: project.startDate,
          endDate: project.endDate ?? ''
        });
      }
    });

    effect(() => {
      const title = this.model().title;
      if (!this.slugManuallyEdited()) {
        const generatedSlug = slugify(title);
        this.model.update(current =>
          current.slug === generatedSlug ? current : { ...current, slug: generatedSlug }
        );
      }
    });
  }

  protected onSlugInput(): void {
    this.slugManuallyEdited.set(true);
  }

  protected readonly projectForm = form(this.model, path => {
    required(path.title, { message: 'Le titre est requis' });
    minLength(path.title, 3, { message: '3 caractères minimum' });
    maxLength(path.title, 150, { message: '150 caractères maximum' });

    required(path.slug, { message: 'Le slug est requis' });
    maxLength(path.slug, 150, { message: '150 caractères maximum' });

    required(path.shortDescription, { message: 'La short description est requise' });

    required(path.description, { message: 'La description est requise' });

    required(path.startDate, { message: 'La date de début est requise' });
  });

  protected async onSubmit(): Promise<void> {
    await submit(this.projectForm, async currentForm => {
      const payload = currentForm().value();
      const id = this.id();

      const result = id
        ? await firstValueFrom(this.projectsApi.update(id, payload))
        : await firstValueFrom(this.projectsApi.create(payload));

      await this.router.navigate(['/projects', result.id]);
    });
  }
}
