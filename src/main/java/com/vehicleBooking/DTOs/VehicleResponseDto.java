package com.vehicleBooking.DTOs;

import lombok.Data;

@Data 
public class VehicleResponseDto {
    
    private String make;
    private String model;
    private int year;
    private double price;
    private double bookingAmount;
}
