import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { RouterLink } from '@angular/router';
import { ProfileApi } from '../data-access/profile-api';
import { AVAILABILITY_LABELS } from '../models/profile';

@Component({
  selector: 'app-profile-view',
  standalone: true,
  imports: [RouterLink],
  templateUrl: './profile-view.html',
  styleUrl: './profile-view.css'
})
export class ProfileView {
  private readonly profileApi = inject(ProfileApi);

  protected readonly availabilityLabels = AVAILABILITY_LABELS;

  protected readonly profileResource = rxResource({
    stream: () => this.profileApi.get()
  });
}
