package com.vehicleBooking.DTOs;

import java.time.LocalDate;
import java.time.LocalDateTime;
import com.vehicleBooking.Models.BookingDetail.BookingStatus;
import lombok.Data;

@Data 
public class BookingDto {
    private LocalDateTime bookingDate;
    private BookingStatus bookingStatus;
    private LocalDate deliveryDate;
    private Long userId;
    private String userEmail;
    private Long vehicleId;
    private String vehicleMake;
    private String vehicleModel;
}
