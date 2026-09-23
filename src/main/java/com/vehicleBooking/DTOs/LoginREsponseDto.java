package com.vehicleBooking.DTOs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor 
@NoArgsConstructor  
public class LoginREsponseDto {
    
    private String email;
    private String role;
    private String jwt;

}
