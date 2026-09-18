import { Component, computed, inject, OnInit, signal } from '@angular/core';
import { RouterLink } from '@angular/router';
import { BookingApiService } from '../../../core/services/booking-api-service';
import { BookingResponse } from '../../../core/models/booking/booking-response';
import { BookingStatus } from '../../../core/models/booking/booking-status';
import { BarberServiceApiService } from '../../../core/services/barber-service-api.service';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { BarberServiceResponse } from '../../../core/models/barber-service/barber-service-response';
import { toSignal } from '@angular/core/rxjs-interop';
import { combineLatest, startWith } from 'rxjs';

@Component({
  selector: 'app-my-bookings',
  imports: [RouterLink, ReactiveFormsModule],
  templateUrl: './my-bookings.component.html',
  styleUrl: './my-bookings.component.css',
})
export class MyBookingsComponent implements OnInit {
  private readonly bookingApiService = inject(BookingApiService);
  private readonly barberServiceApiService = inject(BarberServiceApiService);
  private readonly fb = inject(FormBuilder);

  readonly bookings = signal<BookingResponse[]>([]);
  readonly services = signal<BarberServiceResponse[]>([]);

  readonly loading = signal<boolean>(true);
  readonly loadingServices = signal<boolean>(false);
  readonly errorMessage = signal<string | null>(null);

  readonly deleteErrorMessage = signal<string | null>(null);
  readonly deletingBookingId = signal<number | null>(null);
  readonly bookingToDelete = signal<BookingResponse | null>(null);

  readonly createModalOpen = signal<boolean>(false);
  readonly creatingBooking = signal<boolean>(false);
  readonly creatingBookingErrorMessage = signal<string | null>(null);

  readonly hasBookings = computed(() => this.bookings().length > 0);

  readonly bookingForm = this.fb.nonNullable.group({
    barberServiceId: [0, [Validators.required, Validators.min(1)]],
    appointmentDate: ['', [Validators.required]],
    appointmentTime: ['', [Validators.required]],
  });

  readonly barberServiceId = computed(() => this.bookingForm.controls.barberServiceId);
  readonly appointmentDate = computed(() => this.bookingForm.controls.appointmentDate);
  readonly appointmentTime = computed(() => this.bookingForm.controls.appointmentTime);

  // readonly selectedService = computed(() => {
  //   const seriviceId = Number(this.bookingForm.controls.barberServiceId.value);
  //   return this.services().find((service) => service.id === seriviceId) ?? null;
  // });

  readonly selectedServiceId = toSignal(
    this.bookingForm.controls.barberServiceId.valueChanges.pipe(
      startWith(this.bookingForm.controls.barberServiceId.value),
    ),
    {
      initialValue:0,
    }
  );

  readonly selectedService = computed(()=>{
  const serviceId = Number(this.selectedServiceId());
  return this.services().find((service) => serviceId === serviceId)?? null;
  });

  ngOnInit(): void {
    this.loadMyBookings();
  }

  loadMyBookings(): void {
    this.loading.set(true);
    this.errorMessage.set(null);
    this.deleteErrorMessage.set(null);

    this.bookingApiService.getMyBookings().subscribe({
      next: (bookings) => {
        this.bookings.set(bookings);
        this.loading.set(false);
      },
      error: () => {
        this.errorMessage.set('Non è stato possibile caricare le tue prenotazioni.');
        this.loading.set(false);
      },
    });
  }

  openCreateModal(): void {
    this.creatingBookingErrorMessage.set(null);

    this.bookingForm.reset({
      barberServiceId: 0,
      appointmentDate: '',
      appointmentTime: '',
    });

    this.createModalOpen.set(true);

    if (this.services().length === 0) {
      this.loadServicesForBooking();
    }
  }

  closeCreateModal(): void {
    if (this.creatingBooking()) {
      return;
    }

    this.creatingBookingErrorMessage.set(null);
    this.createModalOpen.set(false);
  }

  loadServicesForBooking(): void {
    this.loadingServices.set(true);
    this.barberServiceApiService.getActiveServices().subscribe({
      next: (services) => {
        this.services.set(services);
        this.loadingServices.set(false);
      },
      error: () => {
        (this.creatingBookingErrorMessage.set(
          'Non è stato possibile caricare i servizi disponibili',
        ),
          this.loadingServices.set(false));
      },
    });
  }

  createBooking(): void {
    if (this.bookingForm.invalid) {
      this.bookingForm.markAllAsTouched();
      return;
    }

    const formValue = this.bookingForm.getRawValue();
    const request = {
      barberServiceId: Number(formValue.barberServiceId),
      appointmentDateTime: `${formValue.appointmentDate}T${formValue.appointmentTime}:00`,
    };
    this.creatingBooking.set(true);
    this.creatingBookingErrorMessage.set(null);
    this.bookingApiService.createBooking(request).subscribe({
      next: (createdBooking) => {
        this.bookings.update((booking) => [createdBooking, ...this.bookings()]);

        this.creatingBooking.set(false);
        this.createModalOpen.set(false);

        this.bookingForm.reset({
          barberServiceId: 0,
          appointmentDate: '',
          appointmentTime: '',
        });
      },
      error: (error) => {
        const message =
          error?.error?.message ||
          'Non è stato possibile creare la prenotazione. Controlla i dati e riprova';
        this.creatingBookingErrorMessage.set(message);
        this.creatingBooking.set(false);
      },
    });
  }

  openDeleteModal(booking: BookingResponse): void {
    this.deleteErrorMessage.set(null);
    this.bookingToDelete.set(booking);
  }

  closeDeleteModal(): void {
    if (this.deletingBookingId()) {
      return;
    }

    this.deleteErrorMessage.set(null);
    this.bookingToDelete.set(null);
  }

  confirmDeleteBooking(): void {
    const booking = this.bookingToDelete();

    if (!booking) {
      return;
    }

    this.deletingBookingId.set(booking.id);
    this.deleteErrorMessage.set(null);

    this.bookingApiService.deleteBooking(booking.id).subscribe({
      next: () => {
        this.bookings.update((bookings) => bookings.filter((item) => item.id !== booking.id));

        this.deletingBookingId.set(null);
        this.bookingToDelete.set(null);
        this.deleteErrorMessage.set(null);
      },
      error: () => {
        this.deleteErrorMessage.set('Non è stato possibile eliminare la prenotazione.');
        this.deletingBookingId.set(null);
      },
    });
  }

  formatDateTime(dateTime: string): string {
    return new Intl.DateTimeFormat('it-IT', {
      dateStyle: 'full',
      timeStyle: 'short',
    }).format(new Date(dateTime));
  }

  getStatusLabel(status: BookingStatus): string {
    switch (status) {
      case BookingStatus.IN_ATTESA:
        return 'In attesa';
      case BookingStatus.CONFERMATA:
        return 'Confermata';
      case BookingStatus.ANNULLATA:
        return 'Annullata';
      default:
        return status;
    }
  }

  getStatusClass(status: BookingStatus): string {
    switch (status) {
      case BookingStatus.IN_ATTESA:
        return 'status-pending';
      case BookingStatus.CONFERMATA:
        return 'status-confirmed';
      case BookingStatus.ANNULLATA:
        return 'status-cancelled';
      default:
        return '';
    }
  }
}
