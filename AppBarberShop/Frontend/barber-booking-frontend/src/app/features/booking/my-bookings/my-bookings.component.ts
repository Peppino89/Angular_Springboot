import { Component, computed, inject, signal } from '@angular/core';
import { BookingApiService } from '../../../core/services/booking-api-service';
import { BookingResponse } from '../../../core/models/booking/booking-response';
import { BookingStatus } from '../../../core/models/booking/booking-status';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-my-bookings',
  imports: [RouterLink],
  templateUrl: './my-bookings.component.html',
  styleUrl: './my-bookings.component.css',
})
export class MyBookingsComponent {
  private readonly bookingApiService = inject(BookingApiService);

  readonly bookings = signal<BookingResponse[]>([]);
  readonly loading = signal<boolean>(true);
  readonly errorMessage = signal<string | null>(null);
  readonly deletingBookingId = signal<number | null>(null);
  readonly bookingsToDelete = signal<BookingResponse | null>(null);

  readonly hasBookings = computed(() => this.bookings().length > 0);

  ngOnInit(): void {
    this.loadMyBookings();
  }

  loadMyBookings(): void {
    this.loading.set(true);
    this.errorMessage.set(null);

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

  openDeleteModal(booking: BookingResponse): void {
    this.bookingsToDelete.set(booking);
  }

  closeDeleteModal() {
    if (this.deletingBookingId()) {
      return;
    }
    this.bookingsToDelete.set(null);
  }

  confirmDeleteBooking(): void {
    const booking = this.bookingsToDelete();

    if (!booking) {
      return;
    }

    this.deletingBookingId.set(booking.id);

    this.bookingApiService.deleteBookings(booking.id).subscribe({
      next: () => {
        this.bookings.update((bookings) => bookings.filter((item) => item.id !== booking.id));
      },
      error: () => {
        this.errorMessage.set('Non è stato possibile eliminare la prenotazione.');
        this.deletingBookingId.set(null);
      },
    });
  }

  formDateTime(dateTime: string): string {
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
        return 'annullata';

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
