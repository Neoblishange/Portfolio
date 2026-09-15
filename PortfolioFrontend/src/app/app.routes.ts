import { Routes } from '@angular/router';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () => import('./features/home/home').then(m => m.Home)
  },
  /**
  {
    path: 'projects',
    loadChildren: () => import('./features/projects/projects.routes').then(m => m.PROJECTS_ROUTES)
  },
  {
    path: 'experiences',
    loadChildren: () => import('./features/experiences/experiences.routes').then(m => m.EXPERIENCES_ROUTES)
  },
  {
    path: 'skills',
    loadChildren: () => import('./features/skills/skills.routes').then(m => m.SKILLS_ROUTES)
  },
  {
    path: 'educations',
    loadChildren: () => import('./features/educations/educations.routes').then(m => m.EDUCATIONS_ROUTES)
  },
  {
    path: 'interests',
    loadChildren: () => import('./features/interests/interests.routes').then(m => m.INTERESTS_ROUTES)
  },
  {
    path: 'profile',
    loadChildren: () => import('./features/profile/profile.routes').then(m => m.PROFILE_ROUTES)
  }
    */
];
