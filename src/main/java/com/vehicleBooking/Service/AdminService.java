package com.vehicleBooking.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vehicleBooking.DTOs.BookingDto;
import com.vehicleBooking.DTOs.UserResponseDto;
import com.vehicleBooking.DTOs.VehicleRegister;

@Service 
public class AdminService {
    
    private final UserService userService;
    private final VehicleService vehicleService;
    private final BookingService bookingService;

    public AdminService(UserService userService, VehicleService vehicleService, BookingService bookingService) {
        this.userService = userService;
        this.vehicleService = vehicleService;
        this.bookingService = bookingService;
    }

    public void deleteUser(Long userId) {
        userService.deleteUser(userId);
    }

    public void deleteVehicle(Long vehicleId) {
        vehicleService.deleteVehicle(vehicleId);
    }

    public void addVehicle(VehicleRegister vehicle) {
        vehicleService.addVehicle(vehicle);
    }

    public void updateVehicle(Long vehicleId, VehicleRegister updatedVehicle) {
        vehicleService.updateVehicle(vehicleId, updatedVehicle);
    }

    public List<UserResponseDto> getAllUsers() {
        return userService.getAllUsers();
    }

    public List<BookingDto> getAllBookings() {
        return bookingService.getAllBookings();
    }


}

