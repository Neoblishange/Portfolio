import { Component, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { ProfileApi } from '../data-access/profile-api';

@Component({
  selector: 'app-profile-view',
  standalone: true,
  templateUrl: './profile-view.html',
  styleUrl: './profile-view.css'
})
export class ProfileView {
  private readonly profileApi = inject(ProfileApi);

  protected readonly profileResource = rxResource({
    stream: () => this.profileApi.get()
  });
}
