import { Component, inject, signal } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { AuthService } from '../../../core/services/auth-service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-login',
  imports: [],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent {

  private readonly fb = inject(FormBuilder);
  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly loading= signal<boolean>(false);
  readonly errorMessage= signal<string | null>(null);
  readonly showPassword= signal<boolean>(false);

  readonly loginForm = this.fb.nonNullable.group({
    username:['', [Validators.required]],
  });

}
