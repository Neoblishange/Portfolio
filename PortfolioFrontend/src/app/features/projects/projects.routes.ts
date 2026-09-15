import { Routes } from '@angular/router';

export const PROJECTS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./projects-list/projects-list').then(m => m.ProjectsList)
  },
  {
    path: 'new',
    loadComponent: () =>
      import('./projects-form/projects-form').then(m => m.ProjectsForm)
  },
  {
    path: ':id',
    loadComponent: () =>
      import('./projects-detail/projects-detail').then(m => m.ProjectsDetail)
  },
  {
    path: ':id/edit',
    loadComponent: () =>
      import('./projects-form/projects-form').then(m => m.ProjectsForm)
  }
];
