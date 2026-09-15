import { Routes } from '@angular/router';

export const EXPERIENCES_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./experiences-list/experiences-list').then(m => m.ExperiencesList)
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./experiences-form/experiences-form').then(m => m.ExperiencesForm)
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./experiences-detail/experiences-detail').then(m => m.ExperiencesDetail)
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./experiences-form/experiences-form').then(m => m.ExperiencesForm)
  }
];
