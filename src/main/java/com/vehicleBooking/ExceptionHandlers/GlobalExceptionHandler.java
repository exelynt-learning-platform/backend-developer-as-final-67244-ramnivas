package com.vehicleBooking.ExceptionHandlers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice 
public class GlobalExceptionHandler {
    
     @ExceptionHandler(UserNotFoundEx.class)
    public ResponseEntity<String> handleUserNotFound(UserNotFoundEx ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(VehicleNotFoundEx.class)
    public ResponseEntity<String> handleVehicleNotFound(VehicleNotFoundEx ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(BookingNotFoundEx.class)
    public ResponseEntity<String> handleBookingError(BookingNotFoundEx ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }
 }
