import { Component, effect, inject, signal } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { form, required, maxLength, submit, FormField } from '@angular/forms/signals';
import { Router } from '@angular/router';
import { firstValueFrom } from 'rxjs';
import { ProfileApi } from '../data-access/profile-api';
import { Availability, AVAILABILITY_LABELS, ProfilePayload } from '../models/profile';

type ProfileFormValue = Omit<ProfilePayload, 'phone' | 'linkedinUrl' | 'githubUrl'> & {
  phone: string;
  linkedinUrl: string;
  githubUrl: string;
};

const EMPTY_PROFILE: ProfileFormValue = {
  firstName: '',
  lastName: '',
  jobTitle: '',
  phone: '',
  email: '',
  linkedinUrl: '',
  githubUrl: '',
  pitch: '',
  yearsOfExperience: 0,
  availability: 'AVAILABLE'
};

@Component({
  selector: 'app-profile-form',
  standalone: true,
  imports: [FormField],
  templateUrl: './profile-form.html',
  styleUrl: './profile-form.css'
})
export class ProfileForm {
  private readonly router = inject(Router);
  private readonly profileApi = inject(ProfileApi);

  protected readonly availabilityLabels = AVAILABILITY_LABELS;
  protected readonly availabilityOptions: Availability[] = [
    'AVAILABLE',
    'NOT_AVAILABLE',
    'OPEN_TO_OPPORTUNITIES'
  ];

  private readonly existingProfileResource = rxResource({
    stream: () => this.profileApi.get()
  });

  protected readonly model = signal<ProfileFormValue>({ ...EMPTY_PROFILE });

  constructor() {
    effect(() => {
      const profile = this.existingProfileResource.value();
      if (profile) {
        this.model.set({
          firstName: profile.firstName,
          lastName: profile.lastName,
          jobTitle: profile.jobTitle,
          phone: profile.phone ?? '',
          email: profile.email,
          linkedinUrl: profile.linkedinUrl ?? '',
          githubUrl: profile.githubUrl ?? '',
          pitch: profile.pitch,
          yearsOfExperience: profile.yearsOfExperience,
          availability: profile.availability
        });
      }
    });
  }

  protected readonly profileForm = form(this.model, path => {
    required(path.firstName, { message: 'Le prénom est requis' });
    maxLength(path.firstName, 100, { message: '100 caractères maximum' });

    required(path.lastName, { message: 'Le nom est requis' });
    maxLength(path.lastName, 100, { message: '100 caractères maximum' });

    required(path.jobTitle, { message: 'Le titre du poste est requis' });
    maxLength(path.jobTitle, 150, { message: '150 caractères maximum' });

    maxLength(path.phone, 30, { message: '30 caractères maximum' });

    required(path.email, { message: "L'email est requis" });
    maxLength(path.email, 255, { message: '255 caractères maximum' });

    maxLength(path.linkedinUrl, 500, { message: '500 caractères maximum' });
    maxLength(path.githubUrl, 500, { message: '500 caractères maximum' });

    required(path.pitch, { message: 'Le pitch est requis' });

    required(path.yearsOfExperience, { message: "Le nombre d'années d'expérience est requis" });
  });

  protected onAvailabilityChange(value: string): void {
    this.model.update(current => ({ ...current, availability: value as Availability }));
  }

  protected async onSubmit(): Promise<void> {
    await submit(this.profileForm, async currentForm => {
      const formValue = currentForm().value();
      const payload: ProfilePayload = {
        ...formValue,
        phone: formValue.phone || null,
        linkedinUrl: formValue.linkedinUrl || null,
        githubUrl: formValue.githubUrl || null
      };

      await firstValueFrom(this.profileApi.update(payload));
      this.router.navigate(['/profile']);
    });
  }
}
