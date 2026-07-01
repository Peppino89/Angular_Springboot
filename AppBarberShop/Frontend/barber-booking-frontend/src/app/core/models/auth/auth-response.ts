import { Role } from './role';

export interface AuthResponse {
  token: string,
  username: string,
  email: string,
  role: Role
}
