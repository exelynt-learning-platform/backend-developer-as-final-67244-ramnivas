package com.vehicleBooking.DTOs;

import lombok.Data;

@Data 
public class VehicleRegister {
    private String make;
    private String model;
    private int year;
    private double price;
    private double bookingAmount;
    private int remainingUnits;
}
