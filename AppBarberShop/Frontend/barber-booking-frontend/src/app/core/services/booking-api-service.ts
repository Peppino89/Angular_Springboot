import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from '../../../environments/environment';
import { Observable } from 'rxjs';
import { BookingResponse } from '../models/booking/booking-response';
import { BookingRequest } from '../models/booking/booking-request';
import { BookingStatus } from '../models/booking/booking-status';

@Injectable({
  providedIn: 'root',
})
export class BookingApiService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl= `${environment.apiUrl}`;

  getMyBookings():Observable<BookingResponse[]>{
    return this.http.get<BookingResponse[]>(`${this.apiUrl}/bookings/user/me`);
}

createBookings(request: BookingRequest): Observable<BookingResponse>{
   return this.http.post<BookingResponse>(this.apiUrl,request);
}

deleteBookings(id: number): Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/${id}`);
}

updateBooking(id: number, status: BookingStatus): Observable<BookingResponse>{
    return this.http.patch<BookingResponse>(`${this.apiUrl}/${id}/status`,{
      status,
    });
}




}
