import { Routes } from '@angular/router';
import { authGuard } from './core/guards/auth-guard';
import { adminGuard } from './core/guards/admin-guard';


export const routes: Routes = [
  {
    path: '',
    loadComponent: () =>
      import('./core/layout/public-layout/public-layout').then((m) => m.PublicLayout),
    children: [
      {
        path: '',
        loadComponent: () => import('./features/home/home.component').then((m) => m.HomeComponent),
      },
      {
        path: 'services',
        loadChildren: () =>
          import('./features/services/services.routes').then((r) => r.SERVICES_ROUTES),
      },
      {
        path: 'not-found',
        loadComponent: () =>
          import('./features/not-found/not-found.component').then((m) => m.NotFoundComponent),
      },
    ],
  },

  {
    path: 'auth',
    loadComponent: () => import('./core/layout/auth-layout/auth-layout').then((m) => m.AuthLayout),
    children: [
      {
        path: '',
        loadChildren: () => import('./features/auth/auth.routes').then((r) => r.AUTH_ROUTES),
      },
    ],
  },

  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () => import('./core/layout/user-layout/user-layout').then((m) => m.UserLayout),
    children: [
      {
        path: 'profile',
        loadChildren: () =>
          import('./features/profile/profile.routes').then((r) => r.PROFILE_ROUTES),
      },
      {
        path: 'bookings',
        loadChildren: () =>
          import('./features/booking/booking.routes').then((r) => r.BOOKING_ROUTES),
      },
    ],
  },

  {
    path: 'admin',
    canActivate: [adminGuard],
    loadComponent: () =>
      import('./core/layout/admin-layout/admin-layout').then((m) => m.AdminLayout),
    children: [
      {
        path: '',
        loadChildren: () => import('./features/admin/admin.routes').then((r) => r.ADMIN_ROUTES),
      },
    ],
  },

  {
    path: '**',
    redirectTo: 'not-found',
  },
];
