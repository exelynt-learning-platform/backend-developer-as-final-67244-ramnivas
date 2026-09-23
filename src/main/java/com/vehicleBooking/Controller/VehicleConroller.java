package com.vehicleBooking.Controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleBooking.Models.Vehicle;
import com.vehicleBooking.Service.VehicleService;

@RestController
@RequestMapping ("/vehicle")
public class VehicleConroller {
    
    private final VehicleService vehicleService;

    public VehicleConroller(VehicleService vehicelService){
        this.vehicleService = vehicelService;
    }

    @GetMapping
    public ResponseEntity<?> getAllVehicle(){
        List<Vehicle> vehicle = vehicleService.getAllVehicles();
        return ResponseEntity.ok(vehicle);
    }

}
