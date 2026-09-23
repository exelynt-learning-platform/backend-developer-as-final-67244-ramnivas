package com.vehicleBooking.ExceptionHandlers;

public class VehicleNotFoundEx extends RuntimeException {
    public VehicleNotFoundEx(String msg){
        super(msg);
    }
}
