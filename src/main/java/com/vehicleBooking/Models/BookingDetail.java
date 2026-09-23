package com.vehicleBooking.Models;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
@Entity 
@Table 
@Data
public class BookingDetail {
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long bookingId;
    private LocalDateTime bookingDate;
    private LocalDate deliveryDate;
    public enum BookingStatus {
        COMPLETED,
        CONFIRMED,
        CANCELLED
    }
    private BookingStatus bookingStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private Vehicle vehicle;

}
