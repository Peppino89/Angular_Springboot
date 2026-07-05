import { Routes } from '@angular/router';
import { authGuard } from '../../core/guards/auth-guard';


export const BOOKING_ROUTES: Routes = [
  {
    path: '',
    canActivate: [authGuard],
    loadComponent: () =>
      import('./my-bookings/my-bookings.component')
        .then((m) => m.MyBookingsComponent),
  },
];
