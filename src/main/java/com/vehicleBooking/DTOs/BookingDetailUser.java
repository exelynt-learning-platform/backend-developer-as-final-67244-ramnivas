package com.vehicleBooking.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.vehicleBooking.Models.BookingDetail.BookingStatus;
import lombok.Data;

@Data 
public class BookingDetailUser {
    private LocalDateTime bookingDate;
    private BookingStatus bookingStatus;
    private LocalDate deliveryDate;
    private VehicleResponseDto vehicle;
}
