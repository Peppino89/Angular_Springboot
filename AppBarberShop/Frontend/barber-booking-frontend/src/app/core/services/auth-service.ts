import { computed, inject, Injectable, signal } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { User } from '../models/auth/user';
import { Role } from '../models/auth/role';
import { RegisterRequest } from '../models/auth/register-request';
import { LoginRequest } from '../models/auth/login-request';
import { AuthResponse } from '../models/auth/auth-response';
import { environment } from '../../../environments/enivorment';

@Injectable({
  providedIn: 'root',
})
export class AuthService {

  private readonly http= inject(HttpClient);

  private readonly tokenSignal= signal<string|null>(localStorage.getItem('token'));
  private readonly userSignal = signal<User|null>(this.loadUserFromStorage());

  readonly token = this.tokenSignal.asReadonly();
  readonly currentUser = this.userSignal.asReadonly();

  readonly isLoggedIn= computed(()=>!!this.tokenSignal());
  readonly isAdmin= computed(()=>this.userSignal()?.role === Role.ADMIN);

  login(request: LoginRequest){
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/login`, request);
  }

  register(request: RegisterRequest){
    return this.http.post<AuthResponse>(`${environment.apiUrl}/auth/register`, request);
  }

  saveAuth(response: AuthResponse):void{

    localStorage.setItem('token', response.token);

    const user: User = {

      username:response.username,
      email: response.email,
      role: response.role,
    }

    localStorage.setItem('user', JSON.stringify(user));

    this.tokenSignal.set(response.token);
    this.userSignal.set(user);

  }

  logout():void{
    localStorage.removeItem('token');
    localStorage.removeItem('user');

    this.tokenSignal.set(null);
    this.userSignal.set(null);
  }

  private loadUserFromStorage(): User | null{
    const userJson = localStorage.getItem('user');

    if (!userJson) {
      return null;
    }
    return JSON.parse(userJson) as User;
  }
}
