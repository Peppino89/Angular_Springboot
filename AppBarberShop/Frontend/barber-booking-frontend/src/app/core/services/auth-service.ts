import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { User } from '../models/auth/user';
import { Role } from '../models/auth/role';
import { RegisterRequest } from '../models/auth/register-request';
import { LoginRequest } from '../models/auth/login-request';
import { AuthResponse } from '../models/auth/auth-response';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private readonly http = inject(HttpClient);

  private readonly tokenSignal = signal<string | null>(localStorage.getItem('token'));
  private readonly userSignal = signal<User | null>(this.loadUserFromStorage());

  readonly token = this.tokenSignal.asReadonly();
  readonly currentUser = this.userSignal.asReadonly();

  readonly isLoggedIn = computed(() => !!this.tokenSignal() && !!this.userSignal());
  readonly isAdmin = computed(() => this.userSignal()?.role === Role.ADMIN);

  constructor() {
     this.checkStoredSession();
  }

  login(request: LoginRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, request);
  }

  register(request: RegisterRequest): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, request);
  }


  isTokenExpired(token:string){
    const payload = this.getTokenPayload(token);

    if(!payload?.exp){
      return true;
    }
    const expirationDate = payload.exp * 1000;//converto in millisecondi perchè il JWT è esperesso in milleSecondi
    const now = Date.now();

    return now >= expirationDate;

  }

  checkStoredSession():void{
    const token = this.tokenSignal();
    if(!token){
      this.logout();
      return;
    }

    if(this.isTokenExpired(token)){
      this.logout();
    }
  }

  private getTokenPayload(token: string): any | null{
    try{
      const payloadBase64 = token.split('.')[1];
      if (!payloadBase64) {
        return null;
      }
      const payloadJson = atob(payloadBase64); //decodifica da Base64 a testo leggibile
      return JSON.parse(payloadJson);
    }catch{
      return null;
    }
  }


  saveAuth(response: AuthResponse): void {
    localStorage.setItem('token', response.token);

    const user: User = {
      username: response.username,
      email: response.email,
      role: response.role,
    };

    localStorage.setItem('user', JSON.stringify(user));

    this.tokenSignal.set(response.token);
    this.userSignal.set(user);
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    this.tokenSignal.set(null);
    this.userSignal.set(null);
  }

  private loadUserFromStorage(): User | null {
    const userJson = localStorage.getItem('user');

    if (!userJson) {
      return null;
    }

    try {
      return JSON.parse(userJson) as User;
    } catch {
      localStorage.removeItem('user');
      localStorage.removeItem('token');
      return null;
    }
  }
}
