package com.vehicleBooking.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.vehicleBooking.DTOs.BookingCreateDto;
import com.vehicleBooking.DTOs.BookingDetailUser;
import com.vehicleBooking.DTOs.BookingDto;
import com.vehicleBooking.DTOs.VehicleRegister;
import com.vehicleBooking.ExceptionHandlers.BookingNotFoundEx;
import com.vehicleBooking.Models.BookingDetail;
import com.vehicleBooking.Models.BookingDetail.BookingStatus;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Models.Vehicle;
import com.vehicleBooking.Repository.BookingRepo;

@Service
public class BookingService{

    private final BookingRepo bookingRepo;
    private final ModelMapper modelMapper;
    private final VehicleService vehicleService;

    public BookingService(BookingRepo bookingRepo, ModelMapper modelMapper, VehicleService vehicleService) {
        this.bookingRepo = bookingRepo;
        this.modelMapper = modelMapper;
        this.vehicleService = vehicleService;
    }

    public List<BookingDto> getAllBookings() {
        return bookingRepo.findAll().stream()
                .map(booking -> modelMapper.map(booking, BookingDto.class))
                .collect(Collectors.toList());
    }

    public BookingDto getBookingById(Long id) {
        return bookingRepo.findById(id).map(booking -> modelMapper.map(booking, BookingDto.class)).orElse(null);
    }

    public BookingDetail createBooking(BookingCreateDto bookingCreateDto, User user) {

        BookingDetail bookingDetail = new BookingDetail();
        Vehicle vehicle = vehicleService.getVehicleById(bookingCreateDto.getVehicleId());
        if (vehicle == null || vehicle.getRemainingUnits() <= 0) {
            return null; 
        }
        vehicle.setRemainingUnits(vehicle.getRemainingUnits()-1);
        vehicleService.saveVehicel(vehicle);
        bookingDetail.setVehicle(vehicle);
        bookingDetail.setUser(user);
        bookingDetail.setDeliveryDate(bookingCreateDto.getDeliveryDate());
        bookingDetail.setBookingDate(LocalDateTime.now());
        // currently setting booking status to CONFIRMED, will use other status when implementing payment gateway
        bookingDetail.setBookingStatus(BookingDetail.BookingStatus.CONFIRMED);

        return bookingRepo.save(bookingDetail);
    }

    public BookingDetailUser cancelBooking(Long id,Long userId) {
        BookingDetail booking = bookingRepo.findById(id)
            .orElseThrow(() ->
                    new BookingNotFoundEx("Booking not found"));

        if (!booking.getUser().getId().equals(userId)) {
            throw new RuntimeException("You cannot cancel this booking");
        }

        // Prevent cancelling an already cancelled booking
        if (booking.getBookingStatus() == BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }

        // Don't allow cancellation after completion
        if (booking.getBookingStatus() == BookingStatus.COMPLETED) {
            throw new RuntimeException("Completed booking cannot be cancelled");
        }

        Vehicle vehicle = booking.getVehicle();

        // Return the vehicle unit to available inventory
        vehicle.setRemainingUnits(
                vehicle.getRemainingUnits() + 1
        );

        // Change booking status
        booking.setBookingStatus(BookingStatus.CANCELLED);

        vehicleService.saveVehicel(vehicle);
        BookingDetail bookingDetail= bookingRepo.save(booking);
        return modelMapper.map(bookingDetail, BookingDetailUser.class);
    }

    public void deleteUserBookings(Long userId) {
        List<BookingDetail> userBookings = bookingRepo.findByUserId(userId);
        for (BookingDetail booking : userBookings) {
            VehicleRegister vehicleRegister = modelMapper.map(booking.getVehicle(), VehicleRegister.class);
            if (vehicleRegister != null) {
                vehicleRegister.setRemainingUnits(vehicleRegister.getRemainingUnits() + 1);
                vehicleService.updateVehicle(booking.getVehicle().getId(), vehicleRegister);
            }
            booking.setBookingStatus(BookingDetail.BookingStatus.CANCELLED);
            bookingRepo.delete(booking);
        }
    }
}