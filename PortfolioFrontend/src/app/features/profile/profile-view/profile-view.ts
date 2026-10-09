import { Component, effect, inject } from '@angular/core';
import { rxResource } from '@angular/core/rxjs-interop';
import { Title } from '@angular/platform-browser';
import { ProfileApi } from '../data-access/profile-api';

@Component({
  selector: 'app-profile-view',
  standalone: true,
  templateUrl: './profile-view.html',
  styleUrl: './profile-view.css'
})
export class ProfileView {
  private readonly profileApi = inject(ProfileApi);
  private readonly title = inject(Title);

  protected readonly profileResource = rxResource({
    stream: () => this.profileApi.get()
  });

  constructor() {
    effect(() => {
      const profile = this.profileResource.value();
      if (profile) {
        this.title.setTitle(`${profile.firstName} ${profile.lastName}`.trim());
      }
    });
  }
}
