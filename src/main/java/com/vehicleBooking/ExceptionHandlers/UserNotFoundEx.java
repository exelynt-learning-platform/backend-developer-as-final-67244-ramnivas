package com.vehicleBooking.ExceptionHandlers;

public class UserNotFoundEx extends RuntimeException {   
    public UserNotFoundEx(String msg){
        super(msg);
    }
}
