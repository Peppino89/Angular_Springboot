import { Component, computed, inject, signal } from '@angular/core';
import {
  AbstractControl,
  FormBuilder, ReactiveFormsModule,
  ValidationErrors,
  Validators,
} from '@angular/forms';
import { AuthService } from '../../../core/services/auth-service';
import { Router, RouterLink } from '@angular/router';
import { RegisterRequest } from '../../../core/models/auth/register-request';


@Component({
  selector: 'app-register',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './register.component.html',
  styleUrl: './register.component.css',
})
export class RegisterComponent {
  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading = signal<boolean>(false);
  readonly errorMessage = signal<string | null>(null);
  readonly showPassword = signal<boolean>(false);
  readonly showConfirmPassword = signal<boolean>(false);

  readonly registerForm = this.fb.nonNullable.group(
    {
      username: ['', [Validators.required, Validators.minLength(3)]],
      email: ['', [Validators.required, Validators.email]],
      password: [
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
      validators: this.passwordsMatchValidator,
    },
  );

  readonly username = computed(() => this.registerForm.controls.username);
  readonly email = computed(() => this.registerForm.controls.email);
  readonly password = computed(() => this.registerForm.controls.password);
  readonly confirmPassword = computed(() => this.registerForm.controls.confirmPassword);

  togglePassword(): void {
    this.showPassword.update((value) => !value);
  }

  toggleConfirmPassword(): void {
    this.showConfirmPassword.update((value) => !value);
  }

  submit(): void {
    if (this.registerForm.invalid) {
      this.registerForm.markAllAsTouched();
      return;
    }

    const formValue = this.registerForm.getRawValue();

    const request: RegisterRequest = {
      username: formValue.username,
      email: formValue.email,
      password: formValue.password,
    };

    this.loading.set(true);
    this.errorMessage.set(null);

    this.authService.register(request).subscribe({
      next: (response) => {
        this.authService.saveAuth(response);
        this.loading.set(false);
        this.router.navigate(['/bookings']);
      },
      error: (error) => {
        this.loading.set(false);
        const message =
          error?.error?.message || 'Registrazione non riuscita. Controlla i dati e riprova';
        this.errorMessage.set(message);
      },
    });
  }

  private passwordsMatchValidator(control: AbstractControl): ValidationErrors | null {
    const password = control.get('password')?.value;
    const confirmedPassword = control.get('confirmPassword')?.value;

    if (!password || !confirmedPassword) return null;

    return password === confirmedPassword ? null : { passwordsMismatch: true };
  }
}
