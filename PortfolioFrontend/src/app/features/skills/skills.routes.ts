import { Routes } from '@angular/router';

export const SKILLS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./skills-list/skills-list').then(m => m.SkillsList)
  },
  {
    path: 'categories',
    loadComponent: () => import('./categories-manager/categories-manager').then(m => m.CategoriesManager)
  },
  {
    path: 'new',
    loadComponent: () => import('./skills-form/skills-form').then(m => m.SkillsForm)
  },
  {
    path: ':id',
    loadComponent: () => import('./skills-detail/skills-detail').then(m => m.SkillsDetail)
  },
  {
    path: ':id/edit',
    loadComponent: () => import('./skills-form/skills-form').then(m => m.SkillsForm)
  }
];
