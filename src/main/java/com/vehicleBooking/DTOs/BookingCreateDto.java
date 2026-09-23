package com.vehicleBooking.DTOs;

import java.time.LocalDate;

import lombok.Data;

@Data 
public class BookingCreateDto {
    
    private Long vehicleId;
    private LocalDate deliveryDate;
}
