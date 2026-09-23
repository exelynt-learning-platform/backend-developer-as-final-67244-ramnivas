package com.vehicleBooking.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vehicleBooking.DTOs.BookingCreateDto;
import com.vehicleBooking.DTOs.BookingDetailUser;
import com.vehicleBooking.Service.UserService;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping("/auth/user")
public class UserController {
    
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/bookVehicle")
    public ResponseEntity<?> addVehicle(@RequestBody BookingCreateDto bookingCreateDto,@AuthenticationPrincipal UserDetails userDetail) {
       
       try {
            userService.addVehicleToUser(bookingCreateDto,userDetail.getUsername() );
       } catch (Exception e) {
            return ResponseEntity.status(500).body("Error adding vehicle: " + e.getMessage());
       }
        return ResponseEntity.ok("Vehicle added successfully");
    }

    @GetMapping("/bookedVehicles")
    public ResponseEntity<?> getBookedVehicles(@AuthenticationPrincipal UserDetails userDetail) {
        try {
            List<BookingDetailUser> bookedVehicles = userService.getBookedVehicles(userDetail.getUsername());
            return ResponseEntity.ok(bookedVehicles);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching booked vehicles: " + e.getMessage());
        }
    }

    @PostMapping("/cancelBooking/{id}")
    public ResponseEntity<?> cancelBooking(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetail) { 
        try {
               BookingDetailUser book  =  userService.cancelBooking(id, userDetail.getUsername());
            return ResponseEntity.ok( book);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching booked vehicles: " + e.getMessage()); 
        }
    }
}