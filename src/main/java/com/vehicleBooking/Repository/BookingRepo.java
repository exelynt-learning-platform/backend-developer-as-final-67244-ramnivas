package com.vehicleBooking.Repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.vehicleBooking.Models.BookingDetail;

public interface BookingRepo extends JpaRepository<BookingDetail, Long> {

    List<BookingDetail> findByUserId(Long userId);
    
}
