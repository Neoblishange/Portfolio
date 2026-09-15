import { Routes } from '@angular/router';

export const EDUCATIONS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./educations-list/educations-list').then(m => m.EducationsList)
  },
  {
    path: 'new',
    loadComponent: () => import('./educations-form/educations-form').then(m => m.EducationsForm)
  },
  {
    path: ':id',
    loadComponent: () => import('./educations-detail/educations-detail').then(m => m.EducationsDetail)
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./educations-form/educations-form').then(m => m.EducationsForm)
  }
];
