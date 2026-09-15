import { Routes } from '@angular/router';

export const INTERESTS_ROUTES: Routes = [
  {
    path: '',
    loadComponent: () => import('./interests-manager/interests-manager').then(m => m.InterestsManager)
  }
];
