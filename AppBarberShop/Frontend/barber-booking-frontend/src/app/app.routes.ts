import { Routes } from '@angular/router';
import { adminGuard } from './core/guards/admin-guard';

export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./features/home/home.component')
        .then((m) => m.HomeComponent),
  },
  {
    path: 'auth/login',
    loadComponent: () =>
      import('./features/auth/login/login.component').then((m) => m.LoginComponent),
  },
  {
    path: 'auth/register',
    loadComponent: () =>
      import('./features/auth/register/register.component').then((m) => m.RegisterComponent),
  },
  {
    path: 'bookings',
    canActivate: [authGuard],
    loadComponent: () =>
                                     import('./features/booking/my-bookings/my-bookings.component')
                                        .then((m) => m.MyBookingsComponent),
  },
  {
    path: 'admin',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./features/admin/admin-dashboard/admin-dashboard.component')
        .then((m) => m.AdminDashboardComponent),
  },

  {
    path: 'not-found',
    loadComponent: () =>
      import('./features/not-found/not-found.component')
        .then((m) => m.NotFoundComponent),
  },
  {
    path: '**',
    redirectTo: 'not-found',
  },
];
