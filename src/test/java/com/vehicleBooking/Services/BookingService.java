package com.vehicleBooking.Services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.modelmapper.ModelMapper;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.vehicleBooking.DTOs.BookingCreateDto;
import com.vehicleBooking.DTOs.BookingDetailUser;
import com.vehicleBooking.Models.BookingDetail;
import com.vehicleBooking.Models.BookingDetail.BookingStatus;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Models.Vehicle;
import com.vehicleBooking.Repository.BookingRepo;
import com.vehicleBooking.Service.BookingService;
import com.vehicleBooking.Service.VehicleService;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private BookingRepo bookingRepo;

    @Mock
    private VehicleService vehicleService;

    private BookingService bookingService;

    @BeforeEach
    void setUp() {
        bookingService = new BookingService(bookingRepo, new ModelMapper(), vehicleService);
    }

    @Test
    void createBooking_shouldSaveBookingAndReduceVehicleUnits() {
        User user = new User();
        user.setId(10L);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(5L);
        vehicle.setRemainingUnits(3);

        BookingCreateDto dto = new BookingCreateDto();
        dto.setVehicleId(5L);
        dto.setDeliveryDate(LocalDate.now().plusDays(2));

        when(vehicleService.getVehicleById(5L)).thenReturn(vehicle);
        when(bookingRepo.save(any(BookingDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDetail savedBooking = bookingService.createBooking(dto, user);

        assertNotNull(savedBooking);
        assertEquals(user, savedBooking.getUser());
        assertEquals(vehicle, savedBooking.getVehicle());
        assertEquals(BookingStatus.CONFIRMED, savedBooking.getBookingStatus());
        assertEquals(2, vehicle.getRemainingUnits());
        verify(vehicleService).saveVehicel(vehicle);
        verify(bookingRepo).save(any(BookingDetail.class));
    }

    @Test
    void cancelBooking_shouldCancelBookingAndRestoreVehicleUnits() {
        User user = new User();
        user.setId(7L);

        Vehicle vehicle = new Vehicle();
        vehicle.setId(8L);
        vehicle.setRemainingUnits(1);

        BookingDetail booking = new BookingDetail();
        booking.setBookingId(12L);
        booking.setUser(user);
        booking.setVehicle(vehicle);
        booking.setBookingStatus(BookingStatus.CONFIRMED);
        booking.setDeliveryDate(LocalDate.now().plusDays(3));

        when(bookingRepo.findById(12L)).thenReturn(Optional.of(booking));
        when(bookingRepo.save(any(BookingDetail.class))).thenAnswer(invocation -> invocation.getArgument(0));

        BookingDetailUser result = bookingService.cancelBooking(12L, 7L);

        assertNotNull(result);
        assertEquals(BookingStatus.CANCELLED, booking.getBookingStatus());
        assertEquals(2, vehicle.getRemainingUnits());
        assertEquals(BookingStatus.CANCELLED, result.getBookingStatus());
        verify(vehicleService).saveVehicel(vehicle);
        verify(bookingRepo).save(booking);
    }
}

