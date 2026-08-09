import { BookingStatus } from './booking-status';

export type BookingResponse = {
  id: number;
  appointmentDateTime: string;
  status: BookingStatus;
  userId: number;
  username: string;
  barberServiceId: number;
  serviceName: string;
};
