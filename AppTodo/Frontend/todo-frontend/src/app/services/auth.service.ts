import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { RegisterRequest } from '../models/register-request';
import { Observable } from 'rxjs';
import { AuthResponse } from '../models/auth-response';
import { LoginRequest } from '../models/login-request';
import { ForgotPasswordRequest } from '../models/forgot-password-request';
import { ResetPasswordRequest } from '../models/reset-password-request';
import { ResetTokenValidationResponse } from '../models/ResetTokenValidationResponse';

@Injectable({
  providedIn: 'root',
})
export class AuthService {
  private http = inject(HttpClient);
  private apiUrl = "http://localhost:8080/api/auth";

  register(request: RegisterRequest):Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.apiUrl}/register`, request);
  }

  login(request:LoginRequest):Observable<AuthResponse>{
    return this.http.post<AuthResponse>(`${this.apiUrl}/login`, request);
  }

  forgotPassword(request: ForgotPasswordRequest):Observable<string> {
    return this.http.post(`${this.apiUrl}/forgot-password`, request,{responseType:'text'});
  }

  resetPassword(request: ResetPasswordRequest):Observable<string>  {
    return this.http.post(`${this.apiUrl}/reset-password`, request,{responseType: 'text'});
  }

  validateResetToken(token: string):Observable<ResetTokenValidationResponse>{
    return this.http.get<ResetTokenValidationResponse>(`${this.apiUrl}/reset-password/validate?token=${token}`);
  }

}
