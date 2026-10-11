package com.example.barberbooking.validation;

import com.example.barberbooking.exception.InvalidBookingSlotException;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public final class BookingRules {

    public static final LocalTime FIRST_SLOT = LocalTime.of(8, 30);
    public static final LocalTime LAST_SLOT = LocalTime.of(19, 30);
    public static final int SLOT_MINUTES = 30;

    private BookingRules(){}

    public static boolean isWorkingDay(LocalDate date){
        DayOfWeek day = date.getDayOfWeek();

        return day != DayOfWeek.SUNDAY && day != DayOfWeek.MONDAY;
    }

    public static void validate(LocalDateTime appointmentDateTime){

        if(!isWorkingDay(appointmentDateTime.toLocalDate())){
            throw new InvalidBookingSlotException("Le prenotazioni sono disponibili dal martedì al sabato");
        }

       LocalTime time = appointmentDateTime.toLocalTime();

        boolean isOnHalfHourGrid =
                (time.getMinute() == 0 || time.getMinute() == 30)
                && time.getSecond() == 0 && time.getNano() == 0;

      if(time.isBefore(FIRST_SLOT) || time.isAfter(LAST_SLOT)||!isOnHalfHourGrid){
            throw new InvalidBookingSlotException("Seleziona uno slot di 30 minuti compreso tra le 08:30 e le 19:30");
      }


    }


    public static List<LocalTime> generateDailySlots(){
        List<LocalTime> slots = new ArrayList<>();

        LocalTime currentSlot = FIRST_SLOT;

        while(!currentSlot.isAfter(LAST_SLOT)){
            slots.add(currentSlot);
           currentSlot = currentSlot.plusMinutes(SLOT_MINUTES);
        }
        return slots;
    }
}
