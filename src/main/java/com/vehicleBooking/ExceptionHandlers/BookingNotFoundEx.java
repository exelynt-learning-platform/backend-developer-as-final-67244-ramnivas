package com.vehicleBooking.ExceptionHandlers;

public class BookingNotFoundEx extends  RuntimeException{
    public BookingNotFoundEx(String msg){
        super(msg);
    }
}