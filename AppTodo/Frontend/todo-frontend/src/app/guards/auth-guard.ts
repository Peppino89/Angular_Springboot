import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { AuthStoreService } from '../stores/auth-store.service';

export const authGuard: CanActivateFn = (route, state) => {

  const authStore = inject(AuthStoreService);
  const router = inject(Router);

  if(authStore.isAuthenticated()){
    return true;
  }

  return router.createUrlTree(['/login']);
};
