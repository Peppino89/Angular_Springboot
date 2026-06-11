import { Component, inject, OnDestroy, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthService } from '../../services/auth.service';
import { passwordMatchValidator } from '../../validators/password-match.validator';

@Component({
  selector: 'app-reset-password',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './reset-password.component.html',
  styleUrl: './reset-password.component.css',
})
export class ResetPasswordComponent implements OnInit, OnDestroy {
  private fb = inject(FormBuilder);
  private router = inject(Router);
  private route = inject(ActivatedRoute);
  private authService = inject(AuthService);
  private expirationTimerId: ReturnType<typeof setTimeout> | null = null;
  linkExpired = signal(false);

  token = signal<string | null>(null);
  loading = signal<boolean>(false);
  successMessage = signal<string | null>(null);
  errorMessage = signal<string | null>(null);

  showPassword = signal<boolean>(false);
  showConfirmPassword = signal<boolean>(false);

  resetPasswordForm = this.fb.group(
    {
      newPassword: [
        '',
        [
          Validators.required,
          Validators.minLength(6),
          Validators.pattern(/^(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$/),
        ],
      ],
      confirmPassword: ['', [Validators.required]],
    },
    {
      validators: passwordMatchValidator('newPassword', 'confirmPassword'),
    },
  );

  ngOnInit(): void {
    const tokenFromUrl = this.route.snapshot.queryParamMap.get('token');

    if (!tokenFromUrl) {
      this.errorMessage.set('Link non valido. Richiedi un nuovo reset password.');
      return;
    }

    this.token.set(tokenFromUrl);

    this.authService.validateResetToken(tokenFromUrl).subscribe({
      next: (response) => {
        const expiresAt = new Date(response.expiresAt).getTime();
        const now = new Date().getTime();
        const delay = expiresAt - now;

        if(delay <= 0){
          this.handleExpiredLink("Link scaduto. Richiedi un nuovo reset password.");
          return;
        }

        this.expirationTimerId = setTimeout(() => {
          this.handleExpiredLink('Link scaduto. Richiedi un nuovo reset password.');
        },delay);

      },
      error: (err) => {
        this.handleExpiredLink(
          err.error?.errorMessage || 'Link non valido o scaduto. Richiedi un nuovo reset password.'
        );
      }
    });

  }

  ngOnDestroy(): void {
    if (this.expirationTimerId) {
      clearTimeout(this.expirationTimerId);
    }
  }

  private handleExpiredLink(message: string): void {
    this.linkExpired.set(true);
    this.errorMessage.set(message);

    alert(message);

    this.router.navigate(['/forgot-password']);
  }
  togglePassword(): void {
    this.showPassword.update((value) => !value);
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword.update((value) => !value);
  }

  onSubmit(): void {
    if (!this.token()) {
      this.errorMessage.set('Token Mancante o non valido');
      return;
    }

    if (this.resetPasswordForm.invalid) {
      this.resetPasswordForm.markAllAsTouched();
      return;
    }

    this.loading.set(true);
    this.errorMessage.set(null);
    this.successMessage.set(null);

    const request = {
      token: this.token()!,
      newPassword: this.resetPasswordForm.value.newPassword!,
      confirmPassword: this.resetPasswordForm.value.confirmPassword!,
    };

    this.authService.resetPassword(request).subscribe({
      next: (response: string) => {
        this.successMessage.set(response);

        setTimeout(() => {
          this.router.navigate(['/login']);
        }, 2000);
      },
      error: (err) => {
        this.errorMessage.set(err.error?.errorMessage || 'Errore durante il reset della password');

        this.loading.set(false);
      },
      complete: () => {
        this.loading.set(false);
      },
    });
  }
}
