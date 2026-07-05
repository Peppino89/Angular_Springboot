import { Component } from '@angular/core';
import { RouterLink } from '@angular/router';

type HeroStat = {
  icon: string;
  value: string;
  label: string;
};

type HowItWorksStep = {
  icon: string,
  title: string,
  description: string
}

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent {
  readonly heroStats: HeroStat[] = [
    {
      icon: 'bx bx-star',
      value: '4.9',
      label: 'Valutazione media',
    },
    {
      icon: 'bx bx-user-check',
      value: '3000+',
      label: 'Clienti soddisfatti',
    },
    {
      icon: 'bx bx-cut',
      value: '20+',
      label: 'Servizi disponibili',
    },
  ];

  readonly howItWorksSteps: HowItWorksStep[] = [
    {
      icon: 'bx bx-cut',
      title: 'Scegli il servizio',
      description: 'Seleziona il taglio, barba o trattamento in base al tuo stile.',
    },
    {
      icon: 'bx bx-calendar',
      title: 'Scegli data e ora',
      description: 'Trova rapidamente lo slot libero più comodo per te.',
    },
    {
      icon: 'bx bx-check-circle',
      title: 'Conferma online',
      description: 'Conferma la prenotazione in pochi secondi, senza chiamate.',
    },
    {
      icon: 'bx bx-star',
      title: 'Goditi il risultato',
      description: 'Arriva in salone e vivi un esperienza barber premium.',
    },
  ];
}
