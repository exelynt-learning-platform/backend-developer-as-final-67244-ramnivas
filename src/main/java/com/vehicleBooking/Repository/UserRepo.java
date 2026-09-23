package com.vehicleBooking.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.vehicleBooking.Models.User;

public interface UserRepo extends JpaRepository<User, Long> {

    User findByEmail(String email);

    List<User> findAllByRole(String string);
    
}
