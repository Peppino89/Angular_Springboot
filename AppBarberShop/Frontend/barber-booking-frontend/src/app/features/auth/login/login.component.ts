import { Component, computed, inject, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth-service';
import {ActivatedRoute, Router, RouterLink} from '@angular/router';
import { LoginRequest } from '../../../core/models/auth/login-request';



@Component({
  selector: 'app-login',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);
  private readonly  route = inject(ActivatedRoute);

  readonly loading = signal<boolean>(false);
  readonly errorMessage = signal<string | null>(null);
  readonly showPassword = signal<boolean>(false);
  readonly infoMessage = signal<string|null>(null);

  readonly loginForm = this.fb.nonNullable.group({
    username: ['', [Validators.required]],
    password: ['', [Validators.required]],
  });

  readonly username = computed(() => this.loginForm.controls.username);
  readonly password = computed(() => this.loginForm.controls.password);

  ngOnInit():void {
    const sessionExpired = this.route.snapshot.queryParamMap.get('sessionExpired');
    if(sessionExpired==='true'){
     this.infoMessage.set("La tua sessione è scaduta. Effettua di nuovo l'accesso.")
    }
  }

  togglePassword(): void {
    this.showPassword.update((value) => !value);
  }

  submit(): void {
    if (this.loginForm.invalid) {
      this.loginForm.markAllAsTouched();
      return;
    }

    const request: LoginRequest = this.loginForm.getRawValue();
    this.loading.set(true);
    this.errorMessage.set(null);
    this.infoMessage.set(null);

    this.authService.login(request).subscribe({
      next: (response) => {
        this.authService.saveAuth(response);
        this.loading.set(false);
        this.router.navigate(['/bookings']);
      },

      error: (error) => {
        this.loading.set(false);
        const message =
          error?.error?.message || 'Login non riuscito. Controlla username e password';
        this.errorMessage.set(message);
      },
    });
  }
}
