import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { AuthService } from '../../services/auth-service';

@Component({
  selector: 'app-public-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.css',
})
export class PublicLayout {

  private readonly authService = inject(AuthService);
  private readonly router = inject(Router);

  readonly menuOpen = signal<boolean>(false);
  readonly token = computed(() => this.authService.token());
  readonly currentUser = computed(() => this.authService.currentUser());
  readonly isAdmin = computed(() => this.authService.isAdmin());
  readonly isLoggedIn = computed(()=>this.authService.isLoggedIn());

  toggleMenu(): void {
    this.menuOpen.update((value) => !value);
  }

  closeMenu(): void {
    this.menuOpen.set(false);
  }

  logout(): void {
    this.authService.logout();
    this.closeMenu();
    this.router.navigate(['/']);
  }
}
