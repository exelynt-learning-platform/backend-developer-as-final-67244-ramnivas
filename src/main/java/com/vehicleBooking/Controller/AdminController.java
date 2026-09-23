package com.vehicleBooking.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleBooking.DTOs.VehicleRegister;
import com.vehicleBooking.Service.AdminService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController 
@RequestMapping ("/auth/admin")
public class AdminController {
    
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/users")
    public ResponseEntity<?> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @GetMapping("/bookings")
    public ResponseEntity<?> getAllBookings() {
        return ResponseEntity.ok(adminService.getAllBookings());
    }

    @PostMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestParam Long userId) {
        try {
            adminService.deleteUser(userId);
            return ResponseEntity.ok("User deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(404).body("User Not found"+e.getMessage());
        }
        
    }
    
    @PostMapping("/deleteVehicle")
    public ResponseEntity<?> deleteVehicle(@RequestParam Long vehicleId) {
        try {
             adminService.deleteVehicle(vehicleId);
            return ResponseEntity.ok("Vehicle deleted successfully");
        } catch (Exception e) {
            return  ResponseEntity.status(404).body("Vehicle Not found"+e.getMessage());
        }
       
    }

    @PostMapping("/addVehicle")
    public ResponseEntity<?> addVehicle(@RequestBody VehicleRegister vehicle) {
        adminService.addVehicle(vehicle);
        return ResponseEntity.ok("Vehicle added successfully");
    }

    @PostMapping("/updateVehicle")
    public ResponseEntity<?> updateVehicle(@RequestParam Long vehicleId, @RequestBody VehicleRegister updatedVehicle) {
        try {
             adminService.updateVehicle(vehicleId, updatedVehicle);
            return ResponseEntity.ok("Vehicle updated successfully");
        } catch (Exception e) {
           return ResponseEntity.status(404).body("Error in upadating vehicle  " + e.getMessage());
        }
       
    }
}
