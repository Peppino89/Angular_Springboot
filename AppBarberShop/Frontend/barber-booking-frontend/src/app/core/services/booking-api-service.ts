import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';

import { Observable } from 'rxjs';

import { environment } from '../../../environments/environment';
import { BookingRequest } from '../models/booking/booking-request';
import { BookingResponse } from '../models/booking/booking-response';
import { BookingStatus } from '../models/booking/booking-status';

@Injectable({
  providedIn: 'root',
})
export class BookingApiService {
  private readonly http = inject(HttpClient);
  private readonly apiUrl = `${environment.apiUrl}/bookings`;

  getMyBookings(): Observable<BookingResponse[]> {
    return this.http.get<BookingResponse[]>(`${this.apiUrl}/user/me`);
  }

  createBooking(request: BookingRequest): Observable<BookingResponse> {
    return this.http.post<BookingResponse>(this.apiUrl, request);
  }

  deleteBooking(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
  }

  updateBookingStatus(id: number, status: BookingStatus): Observable<BookingResponse> {
    return this.http.patch<BookingResponse>(`${this.apiUrl}/${id}/status`, {
      status,
    });
  }
}
