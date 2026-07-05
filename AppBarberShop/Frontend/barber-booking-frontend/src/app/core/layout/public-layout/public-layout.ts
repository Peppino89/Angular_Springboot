import { Component, signal } from '@angular/core';
import { RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';

@Component({
  selector: 'app-public-layout',
  imports: [RouterOutlet, RouterLink, RouterLinkActive],
  templateUrl: './public-layout.html',
  styleUrl: './public-layout.css',
})
export class PublicLayout {
  readonly  menuOpen = signal<boolean>(false);

  toggleMenu():void{
    this.menuOpen.update(value => !value);
  }

  closeMenu():void{
    this.menuOpen.set(false);
  }
}
