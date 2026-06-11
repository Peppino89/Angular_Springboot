import { computed, inject, Injectable, signal } from '@angular/core';
import { Router } from '@angular/router';
import { User } from '../models/user';
import { HttpClient } from '@angular/common/http';
import { AuthResponse } from '../models/auth-response';


@Injectable({
  providedIn: 'root',
})
export class AuthStoreService {
  private router = inject(Router);

  private readonly tokenKey = 'token';
  private readonly usernameKey = 'username';
  private readonly emailKey = 'email';


  user = signal<User|null>(null);
  token = signal<string|null>(localStorage.getItem('token'));

  isAuthenticated = computed(()=>!!this.token() && this.user!==null);

  constructor() {
    this.restoreSession();
  }
  setSession(response: AuthResponse):void {
    localStorage.setItem(this.tokenKey, response.token);
    localStorage.setItem(this.usernameKey, response.username);
    localStorage.setItem(this.emailKey, response.email);

    this.token.set(response.token);
    this.user.set({
      username: response.username,
      email: response.email,
    });
  }

  restoreSession():void {
    const token = localStorage.getItem(this.tokenKey);
    const username = localStorage.getItem(this.usernameKey);
    const email = localStorage.getItem(this.emailKey);

    if (token && email && username && email) {
      this.token.set(token);
      this.user.set({ username, email });
    }
  }

  logout():void {
    localStorage.removeItem(this.tokenKey);
    localStorage.removeItem(this.usernameKey);
    localStorage.removeItem(this.emailKey);

    this.router.navigate(['/login']);
  }



}
