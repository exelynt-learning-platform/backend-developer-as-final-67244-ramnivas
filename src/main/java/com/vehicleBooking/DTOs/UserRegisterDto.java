package com.vehicleBooking.DTOs;

import lombok.Data;

@Data 
public class UserRegisterDto {
    private String name;
    private String email;
    private String password;
    private String phoneNumber;

}
