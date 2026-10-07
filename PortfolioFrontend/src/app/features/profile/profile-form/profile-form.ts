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
    required(path.firstName, { message: 'First name is required' });
    maxLength(path.firstName, 100, { message: 'Maximum 100 characters' });
    required(path.lastName, { message: 'Last name is required' });
    maxLength(path.lastName, 100, { message: 'Maximum 100 characters' });
    required(path.jobTitle, { message: 'Job title is required' });
    maxLength(path.jobTitle, 150, { message: 'Maximum 150 characters' });
    maxLength(path.phone, 30, { message: 'Maximum 30 characters' });
    required(path.email, { message: 'Email is required' });
    maxLength(path.email, 255, { message: 'Maximum 255 characters' });
    maxLength(path.linkedinUrl, 500, { message: 'Maximum 500 characters' });
    maxLength(path.githubUrl, 500, { message: 'Maximum 500 characters' });
    required(path.pitch, { message: 'Summary is required' });
    required(path.yearsOfExperience, { message: 'Years of experience is required' });
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
