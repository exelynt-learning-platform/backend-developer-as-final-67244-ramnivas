package com.vehicleBooking.Repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vehicleBooking.Models.Vehicle;

public interface VehicleRepo extends JpaRepository<Vehicle, Long> {
    
}
