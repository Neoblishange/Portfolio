import { Component, computed, effect, inject, signal } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { form, required, maxLength, validate, submit, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, of } from 'rxjs';
import { ExperiencesApi } from '../data-access/experiences-api';
import { ExperiencePayload } from '../models/experience';

type ExperienceFormValue = Omit<ExperiencePayload, 'location' | 'endDate'> & {
  location: string;
  endDate: string;
};

const EMPTY_EXPERIENCE: ExperienceFormValue = {
  company: '',
  position: '',
  location: '',
  description: '',
  startDate: '',
  endDate: '',
  current: false
};

@Component({
  selector: 'app-experiences-form',
  standalone: true,
  imports: [FormField],
  templateUrl: './experiences-form.html',
  styleUrl: './experiences-form.css'
})
export class ExperiencesForm {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly experiencesApi = inject(ExperiencesApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => (params.get('id') ? Number(params.get('id')) : null)))
  );

  protected readonly isEditMode = computed(() => this.id() !== null);

  private readonly existingExperienceResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => (id ? this.experiencesApi.getById(id) : of(undefined))
  });

  protected readonly model = signal<ExperienceFormValue>({ ...EMPTY_EXPERIENCE });

  constructor() {
    effect(() => {
      const experience = this.existingExperienceResource.value();
      if (experience) {
        this.model.set({
          company: experience.company,
          position: experience.position,
          location: experience.location ?? '',
          description: experience.description,
          startDate: experience.startDate,
          endDate: experience.endDate ?? '',
          current: experience.current
        });
      }
    });

    effect(() => {
      if (this.model().current && this.model().endDate !== '') {
        this.model.update(current => ({ ...current, endDate: '' }));
      }
    });
  }

  protected readonly experienceForm = form(this.model, path => {
    required(path.company, { message: "L'entreprise est requise" });
    maxLength(path.company, 150, { message: '150 caractères maximum' });

    required(path.position, { message: 'Le poste est requis' });
    maxLength(path.position, 150, { message: '150 caractères maximum' });

    maxLength(path.location, 150, { message: '150 caractères maximum' });

    required(path.description, { message: 'La description est requise' });

    required(path.startDate, { message: 'La date de début est requise' });

    validate(path.endDate, ({ value, valueOf }) => {
      const isCurrent = valueOf(path.current);
      if (!isCurrent && !value()) {
        return { kind: 'required', message: 'La date de fin est requise si le poste n\'est plus actuel' };
      }
      return null;
    });
  });

  protected async onSubmit(): Promise<void> {
    await submit(this.experienceForm, async currentForm => {
      const formValue = currentForm().value();
      const payload: ExperiencePayload = {
        ...formValue,
        location: formValue.location || null,
        endDate: formValue.current ? null : (formValue.endDate || null)
      };

      const id = this.id();
      const result = id
        ? await firstValueFrom(this.experiencesApi.update(id, payload))
        : await firstValueFrom(this.experiencesApi.create(payload));

      this.router.navigate(['/experiences', result.id]);
    });
  }
}
