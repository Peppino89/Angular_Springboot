import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BarberServiceApiService } from '../../core/services/barber-service-api.service';
import { BarberServiceResponse } from '../../core/models/barber-service/barber-service-response';
import { environment } from '../../../environments/environment';

type HeroStat = {
  icon: string;
  value: string;
  label: string;
};

type HowItWorksStep = {
  icon: string;
  title: string;
  description: string;
}

/*type FeaturedService = {
  icon:string;
  name: string;
  description: string;
  duration:string;
  price:string;
}*/

@Component({
  selector: 'app-home',
  imports: [RouterLink],
  templateUrl: './home.component.html',
  styleUrl: './home.component.css',
})
export class HomeComponent implements OnInit {

  private readonly barberServiceApi = inject(BarberServiceApiService);

  readonly services= signal<BarberServiceResponse[]>([]);
  readonly servicesLoading= signal<boolean>(true);
  readonly servicesErrorMessage= signal<string|null>(null);

  readonly featuredServices= computed(()=>this.services().slice(0,3));

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
      description: 'Seleziona taglio, barba o trattamento in base al tuo stile.',
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
      description: 'Arriva in salone e vivi un’esperienza barber premium.',
    },
  ];
  // readonly featuredServices: FeaturedService[] = [
  //   {
  //     icon: 'bx bx-cut',
  //     name: 'Taglio Uomo',
  //     description:
  //       'Taglio moderno o classico, studiato in base al tuo stile e alla forma del viso.',
  //     duration: '30 min',
  //     price: '€18',
  //   },
  //   {
  //     icon: 'bx bx-face',
  //     name: 'Barba',
  //     description: 'Definizione barba, rifinitura dei contorni e trattamento premium.',
  //     duration: '25 min',
  //     price: '€15',
  //   },
  //   {
  //     icon: 'bx bx-crown',
  //     name: 'Taglio + Barba',
  //     description: 'Esperienza completa per un look curato, elegante e sempre ordinato.',
  //     duration: '55 min',
  //     price: '€30',
  //   },
  // ];

ngOnInit():void {
  this.loadFeaturedServices();
}

loadFeaturedServices():void {
  this.servicesLoading.set(true);
  this.servicesErrorMessage.set(null);

  this.barberServiceApi.getActiveServices().subscribe({
    next: services => {
      this.services.set(services);
      this.servicesLoading.set(false);
    },
    error: error => {
      this.servicesErrorMessage.set('Non è stato possibile caricare i servizi in evidenza.');
      this.servicesLoading.set(false);
    }
  });

}

getImageUrl(imageUrl:string | null):string {
  if(!imageUrl)return '';

  return `${environment.backendUrl}${imageUrl}`;
}



}
