package com.vehicleBooking.Service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.vehicleBooking.DTOs.VehicleRegister;
import com.vehicleBooking.Models.Vehicle;
import com.vehicleBooking.Repository.VehicleRepo;

@Service 
public class VehicleService {
    
    private final VehicleRepo vehicleRepo;

    public VehicleService(VehicleRepo vehicleRepo) {
        this.vehicleRepo = vehicleRepo;
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepo.findAll();
    }

    public Vehicle getVehicleById(Long id) {
        return vehicleRepo.findById(id).orElse(null);
    }

    public void updateVehicle(Long id, VehicleRegister vehicle) {
        Vehicle existingVehicle = vehicleRepo.findById(id)
                                    .orElseThrow(() -> new RuntimeException("Vehicle not found"));
        // This is method for updating the vehicle object is right when we are getting the data from
        //  form based input model because there all feilds are sent if they changed or not but for 
        // hiting this with postman or any api based application it is not necessary that user is sending 
        // the complete object so this can fail
        // if (existingVehicle != null) {
        //     existingVehicle.setModel(vehicle.getModel());
        //     existingVehicle.setMake(vehicle.getMake());
        //     existingVehicle.setPrice(vehicle.getPrice());
        //     existingVehicle.setBookingAmount(vehicle.getBookingAmount());
        //     existingVehicle.setRemainingUnits(vehicle.getRemainingUnits());
        //     vehicleRepo.save(existingVehicle);
        // }

        //  so for that we are implementing this nested cheacker
        if (vehicle.getModel() != null) existingVehicle.setModel(vehicle.getModel());
        
        if (vehicle.getMake() != null)  existingVehicle.setMake(vehicle.getMake());
        
        if (vehicle.getPrice() != 0)  existingVehicle.setPrice(vehicle.getPrice());
        
        if (vehicle.getBookingAmount() != 0) existingVehicle.setBookingAmount(vehicle.getBookingAmount());
        
        if (vehicle.getRemainingUnits() != 0) existingVehicle.setRemainingUnits(vehicle.getRemainingUnits());

        if(vehicle.getYear() != 0) existingVehicle.setYear(vehicle.getYear());
        
        vehicleRepo.save(existingVehicle);
    }

    public void deleteVehicle(Long vehicleId) {
        vehicleRepo.deleteById(vehicleId);
    }

    public void addVehicle(VehicleRegister vehicle) {
        Vehicle newVehicle = new Vehicle();
        newVehicle.setModel(vehicle.getModel());
        newVehicle.setMake(vehicle.getMake());
        newVehicle.setPrice(vehicle.getPrice());
        newVehicle.setYear(vehicle.getYear());
        newVehicle.setBookingAmount(vehicle.getBookingAmount());
        newVehicle.setRemainingUnits(vehicle.getRemainingUnits());
        vehicleRepo.save(newVehicle);
    }

    public void saveVehicel(Vehicle vehicle) {
        if(vehicle != null) vehicleRepo.save(vehicle);
    }
}

