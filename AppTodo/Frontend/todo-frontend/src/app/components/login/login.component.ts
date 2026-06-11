import { Component, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../services/auth.service';
import { AuthStoreService } from '../../stores/auth-store.service';
import { Router, RouterLink } from '@angular/router';
import { AuthResponse } from '../../models/auth-response';


@Component({
  selector: 'app-login',
  imports: [ReactiveFormsModule, RouterLink],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private fb = inject(FormBuilder);
  private authService = inject(AuthService);
  private authStore = inject(AuthStoreService);
  private router = inject(Router);

  errorMessage = signal<string | null>(null);
  loading = signal<boolean>(false);
  showPassword= signal<boolean>(false);

  loginForm = this.fb.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  onSubmit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);

    const request = {
      username: this.loginForm.value.username!,
      password: this.loginForm.value.password!,
    };

    this.authService.login(request).subscribe({
      next: (response: AuthResponse): void => {
        this.authStore.setSession(response);
        this.router.navigate(['/todos']);
      },
      error: (error) => {
        this.errorMessage.set(error.error?.errorMessage || 'Login Fallito');
        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }

  togglePassword(): void {
    this.showPassword.update(value => !value);
  }
}
