import { Component, computed, effect, inject, signal } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { form, required, minLength, maxLength, submit, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, of } from 'rxjs';
import { ProjectsApi } from '../data-access/projects-api';
import { SkillsApi } from '../../skills/data-access/skills-api';
import { ProjectPayload } from '../models/project';
import { Skill } from '../../skills/models/skill';

type ProjectFormValue = Omit<ProjectPayload, 'functionalities'> & {
  functionalitiesText: string;
};

const EMPTY_PROJECT: ProjectFormValue = {
  title: '',
  slug: '',
  projectType: '',
  projectContext: '',
  shortDescription: '',
  description: '',
  functionalitiesText: '',
  startDate: '',
  endDate: '',
  skills: []
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
  private readonly skillsApi = inject(SkillsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(
      map(params => (params.get('id') ? Number(params.get('id')) : null))
    )
  );

  protected readonly isEditMode = computed(() => this.id() !== null);

  protected readonly skillsResource = rxResource({
    stream: () => this.skillsApi.getAll()
  });

  private readonly existingProjectResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => (id ? this.projectsApi.getById(id) : of(undefined))
  });

  protected readonly model = signal<ProjectFormValue>({ ...EMPTY_PROJECT });

  private readonly slugManuallyEdited = signal(false);

  constructor() {
    effect(() => {
      const project = this.existingProjectResource.value();
      if (project) {
        this.slugManuallyEdited.set(true);
        this.model.set({
          title: project.title,
          slug: project.slug,
          projectType: project.projectType,
          projectContext: project.projectContext,
          shortDescription: project.shortDescription,
          description: project.description,
          functionalitiesText: project.functionalities.join('\n'),
          startDate: project.startDate,
          endDate: project.endDate ?? '',
          skills: project.skills
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

  protected isSkillSelected(skillId: number): boolean {
    return this.model().skills.some(skill => skill.id === skillId);
  }

  protected onSkillChange(skill: Skill, checked: boolean): void {
    this.model.update(current => ({
      ...current,
      skills: checked
        ? [...current.skills.filter(selected => selected.id !== skill.id), skill]
        : current.skills.filter(selected => selected.id !== skill.id)
    }));
  }

  protected onSkillCheckboxChange(skill: Skill, event: Event): void {
    if (event.target instanceof HTMLInputElement) {
      this.onSkillChange(skill, event.target.checked);
    }
  }

  protected onFunctionalitiesInput(event: Event): void {
    const target = event.target;
    if (target instanceof HTMLTextAreaElement) {
      this.model.update(current => ({
        ...current,
        functionalitiesText: target.value
      }));
    }
  }

  protected readonly projectForm = form(this.model, path => {
    required(path.title, { message: 'Title is required' });
    minLength(path.title, 3, { message: 'Minimum 3 characters' });
    maxLength(path.title, 150, { message: 'Maximum 150 characters' });
    required(path.slug, { message: 'Slug is required' });
    maxLength(path.slug, 150, { message: 'Maximum 150 characters' });
    required(path.shortDescription, { message: 'Short description is required' });
    required(path.projectType, { message: 'Project type is required' });
    required(path.projectContext, { message: 'Project context is required' });
    required(path.description, { message: 'Description is required' });
    required(path.startDate, { message: 'Start date is required' });
  });

  protected async onSubmit(): Promise<void> {
    await submit(this.projectForm, async currentForm => {
      const { functionalitiesText, ...project } = currentForm().value();
      const payload: ProjectPayload = {
        ...project,
        functionalities: functionalitiesText
          .split('\n')
          .map(item => item.trim())
          .filter(Boolean)
      };
      const id = this.id();

      const result = id
        ? await firstValueFrom(this.projectsApi.update(id, payload))
        : await firstValueFrom(this.projectsApi.create(payload));

      await this.router.navigate(['/projects', result.id]);
    });
  }
}
