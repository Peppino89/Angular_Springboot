package com.example.barberbooking.specification;

import com.example.barberbooking.entity.Booking;
import com.example.barberbooking.enums.BookingStatus;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalDateTime;


public class BookingSpecification {

    public static Specification<Booking> hasStatus(BookingStatus status){
        return (root, query, criteriaBuilder) -> {
            if(status == null){
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }


    public static Specification<Booking> hasUsername(String username){
        return (root,query,criteriaBuilder) -> {

            if(username == null || username.isBlank()){
                return criteriaBuilder.conjunction();
            }

            return criteriaBuilder.like(
                    criteriaBuilder.lower(root.get("user").get("username")),
                    "%"+username+"%"
            );

        };
    }

    public static Specification<Booking> hasDate(LocalDate date){
        return (root, query, criteriaBuilder) -> {
            if(date == null){
                return criteriaBuilder.conjunction();
            }

            LocalDateTime start =  date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();

            return criteriaBuilder.between(root.get("appointmentDateTime"), start, end);

        };
    }

}
