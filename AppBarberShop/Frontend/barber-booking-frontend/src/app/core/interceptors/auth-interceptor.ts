import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { AuthService } from '../services/auth-service';
import { catchError, throwError } from 'rxjs';
import { Router } from '@angular/router';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const authService: AuthService = inject(AuthService);
  const router: Router = inject(Router);
  const normalizedUrl:string = req.url.endsWith('/') ? req.url.slice(0,-1) : req.url ;

  const isPublicrequest =
    req.url.includes('/api/auth/')||
    (req.method === "GET" && normalizedUrl.endsWith('/api/services')) ||
    req.url.includes('/uploads/');

  if(isPublicrequest){
    return next(req);
  }

  const token= authService.token();

  if (!token) {
    return next(req);
  }

  if(authService.isTokenExpired(token)){
    authService.logout();
    router.navigate(['/auth/login'],{
      queryParams:{
        sessionExpired:true,
      },
    });

    return throwError(()=>new Error('Token Scaduto'));
  }

  const authRequest = req.clone({
    setHeaders: {
      Authorization: `Bearer ${token}`,
    },
  });

  return next(authRequest).pipe(
    catchError((error: HttpErrorResponse)=>{
      console.log('Errore intercettato:', error.status);
      if(error.status === 401){
        authService.logout();
        router.navigate(['/auth/login'], {
          queryParams:{
            sessionExpired:true,
          }
        })
      }
      return throwError(()=>error);
    })
  );


};
