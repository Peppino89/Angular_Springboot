import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { Observable } from 'rxjs';
import { BarberServiceResponse } from '../models/barber-service/barber-service-response';
import { environment } from '../../../environments/environment';

@Injectable({
  providedIn: 'root',
})
export class BarberServiceApiService {

  private readonly http = inject(HttpClient);
  private readonly apiUrl=`${environment.apiUrl}/services`;

  getActiveServices():Observable<BarberServiceResponse[]>{
    return this.http.get<BarberServiceResponse[]>(this.apiUrl);
  }

}
