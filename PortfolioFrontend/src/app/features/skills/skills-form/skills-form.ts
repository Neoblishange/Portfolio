import { Component, computed, effect, inject, signal } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { form, required, maxLength, submit, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, of } from 'rxjs';
import { SkillsApi } from '../data-access/skills-api';
import { CategoriesApi } from '../data-access/categories-api';
import { SkillPayload } from '../models/skill';

type SkillFormValue = Omit<SkillPayload, 'level'> & { level: string };

const EMPTY_SKILL: SkillFormValue = {
  name: '',
  level: '',
  categoryId: 0,
  displayOrder: 0
};

@Component({
  selector: 'app-skills-form',
  standalone: true,
  imports: [FormField],
  templateUrl: './skills-form.html',
  styleUrl: './skills-form.css'
})
export class SkillsForm {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly skillsApi = inject(SkillsApi);
  private readonly categoriesApi = inject(CategoriesApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => (params.get('id') ? Number(params.get('id')) : null)))
  );

  protected readonly isEditMode = computed(() => this.id() !== null);

  protected readonly categoriesResource = rxResource({
    stream: () => this.categoriesApi.getAll()
  });

  private readonly existingSkillResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => (id ? this.skillsApi.getById(id) : of(undefined))
  });

  protected readonly model = signal<SkillFormValue>({ ...EMPTY_SKILL });

  constructor() {
    effect(() => {
      const skill = this.existingSkillResource.value();
      if (skill) {
        this.model.set({
          name: skill.name,
          level: skill.level ?? '',
          categoryId: skill.category.id,
          displayOrder: skill.displayOrder
        });
      }
    });
  }

  protected readonly skillForm = form(this.model, path => {
    required(path.name, { message: 'Le nom est requis' });
    maxLength(path.name, 100, { message: '100 caractères maximum' });

    maxLength(path.level, 50, { message: '50 caractères maximum' });

    required(path.categoryId, { message: 'La catégorie est requise' });
  });

  protected onCategoryChange(value: string): void {
    this.model.update(current => ({ ...current, categoryId: Number(value) }));
  }

  protected async onSubmit(): Promise<void> {
    await submit(this.skillForm, async currentForm => {
      const formValue = currentForm().value();
      const payload: SkillPayload = {
        ...formValue,
        level: formValue.level || null
      };

      const id = this.id();
      const result = id
        ? await firstValueFrom(this.skillsApi.update(id, payload))
        : await firstValueFrom(this.skillsApi.create(payload));

      this.router.navigate(['/skills', result.id]);
    });
  }
}
