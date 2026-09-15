import { Routes } from '@angular/router';

export const PROFILE_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./profile-view/profile-view').then(m => m.ProfileView)
  },
  {
    path: 'edit',
    loadComponent: () => import('./profile-form/profile-form').then(m => m.ProfileForm)
  }
];
