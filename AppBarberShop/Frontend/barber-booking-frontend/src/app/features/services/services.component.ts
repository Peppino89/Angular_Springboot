import { Component, computed, inject, signal } from '@angular/core';
import { BarberServiceResponse } from '../../core/models/barber-service/barber-service-response';
import { BarberServiceApiService } from '../../core/services/barber-service-api.service';
import { environment } from '../../../environments/environment';
import { RouterLink } from '@angular/router';



@Component({
  selector: 'app-services',
  imports: [RouterLink],
  templateUrl: './services.component.html',
  styleUrl: './services.component.css',
})
export class ServicesComponent {
  private readonly barberServiceApi = inject(BarberServiceApiService);

  readonly services = signal<BarberServiceResponse[]>([]);
  readonly loading = signal<boolean>(true);
  readonly errorMessage = signal<string | null>(null);

  readonly hasServices = computed(() => this.services().length > 0);

  ngOnInit(): void {
    this.loadServices();
  }

  loadServices(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

    this.barberServiceApi.getActiveServices().subscribe({
      next: (services) => {
        this.services.set(services);
        this.loading.set(false);
      },
      error: (error) => {
        this.errorMessage.set('Non è stato possibile caricare i servizi');
        this.loading.set(false);
      },
    });
  }

  getImageUrl(imageUrl: string | null): string {
    if (!imageUrl) {
      return '';
    }
    return `${environment.backendUrl}${imageUrl}`;
  }
}
