import { Component, computed, effect, inject, signal } from '@angular/core';
import { rxResource, toSignal } from '@angular/core/rxjs-interop';
import { form, required, maxLength, submit, FormField } from '@angular/forms/signals';
import { ActivatedRoute, Router } from '@angular/router';
import { firstValueFrom, map, of } from 'rxjs';
import { EducationsApi } from '../data-access/educations-api';
import { EducationPayload } from '../models/education';

type EducationFormValue = Omit<EducationPayload, 'location' | 'endDate' | 'description'> & {
  location: string;
  endDate: string;
  description: string;
};

const EMPTY_EDUCATION: EducationFormValue = {
  schoolName: '',
  location: '',
  startDate: '',
  endDate: '',
  degree: '',
  description: ''
};

@Component({
  selector: 'app-educations-form',
  standalone: true,
  imports: [FormField],
  templateUrl: './educations-form.html',
  styleUrl: './educations-form.css'
})
export class EducationsForm {
  private readonly route = inject(ActivatedRoute);
  private readonly router = inject(Router);
  private readonly educationsApi = inject(EducationsApi);

  private readonly id = toSignal(
    this.route.paramMap.pipe(map(params => (params.get('id') ? Number(params.get('id')) : null)))
  );

  protected readonly isEditMode = computed(() => this.id() !== null);

  private readonly existingEducationResource = rxResource({
    params: () => this.id(),
    stream: ({ params: id }) => (id ? this.educationsApi.getById(id) : of(undefined))
  });

  protected readonly model = signal<EducationFormValue>({ ...EMPTY_EDUCATION });

  constructor() {
    effect(() => {
      const education = this.existingEducationResource.value();
      if (education) {
        this.model.set({
          schoolName: education.schoolName,
          location: education.location ?? '',
          startDate: education.startDate,
          endDate: education.endDate ?? '',
          degree: education.degree,
          description: education.description ?? ''
        });
      }
    });
  }

  protected readonly educationForm = form(this.model, path => {
    required(path.schoolName, { message: "L'établissement est requis" });
    maxLength(path.schoolName, 200, { message: '200 caractères maximum' });

    maxLength(path.location, 150, { message: '150 caractères maximum' });

    required(path.startDate, { message: 'La date de début est requise' });

    required(path.degree, { message: 'Le diplôme est requis' });
    maxLength(path.degree, 200, { message: '200 caractères maximum' });
  });

  protected async onSubmit(): Promise<void> {
    await submit(this.educationForm, async currentForm => {
      const formValue = currentForm().value();
      const payload: EducationPayload = {
        ...formValue,
        location: formValue.location || null,
        endDate: formValue.endDate || null,
        description: formValue.description || null
      };

      const id = this.id();
      const result = id
        ? await firstValueFrom(this.educationsApi.update(id, payload))
        : await firstValueFrom(this.educationsApi.create(payload));

      this.router.navigate(['/educations', result.id]);
    });
  }
}
