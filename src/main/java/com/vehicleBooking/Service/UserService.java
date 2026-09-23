package com.vehicleBooking.Service;

import java.util.List;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vehicleBooking.DTOs.BookingCreateDto;
import com.vehicleBooking.DTOs.BookingDetailUser;
import com.vehicleBooking.DTOs.UserInfoDetail;
import com.vehicleBooking.DTOs.UserRegisterDto;
import com.vehicleBooking.DTOs.UserResponseDto;
import com.vehicleBooking.ExceptionHandlers.BookingNotFoundEx;
import com.vehicleBooking.ExceptionHandlers.UserNotFoundEx;
import com.vehicleBooking.ExceptionHandlers.VehicleNotFoundEx;
import com.vehicleBooking.Models.BookingDetail;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Repository.UserRepo;

@Service 
public class UserService implements UserDetailsService {
    
    private final ModelMapper modelMapper;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoderService;
    private final BookingService bookingService;
    
    public UserService(UserRepo userRepo, PasswordEncoder passwordEncoderService, BookingService bookingService, ModelMapper modelMapper) {
        this.userRepo = userRepo;
        this.passwordEncoderService = passwordEncoderService;
        this.bookingService = bookingService;
        this.modelMapper = modelMapper;
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    public List<UserResponseDto> getAllUsers() {
        List<User> user = userRepo.findAllByRole("ROLE_USER");
        if(user==null) return null;
        return user.stream()
                .map(use -> modelMapper.map(use, UserResponseDto.class))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
        return new UserInfoDetail(user);
    }

    public User registerUser(UserRegisterDto userRegisterDto) {
        User existingUser = userRepo.findByEmail(userRegisterDto.getEmail());
        if (existingUser != null) {
            return null; 
        }

        User newUser = new User();
        newUser.setName(userRegisterDto.getName());
        newUser.setEmail(userRegisterDto.getEmail());
        newUser.setPassword(passwordEncoderService.encode(userRegisterDto.getPassword()));
        newUser.setPhoneNumber(userRegisterDto.getPhoneNumber());
        newUser.setRole("ROLE_USER"); 

        return userRepo.save(newUser);
    }

    public void addVehicleToUser(BookingCreateDto bookingCreateDto, String email) {
       User user = userRepo.findByEmail(email);

       if(user==null){
       throw new UserNotFoundEx("User with email "+ email + " is not exist" );
       }
       try {
            BookingDetail bookingDetail = bookingService.createBooking(bookingCreateDto, user);
            if(bookingDetail==null){
                throw new VehicleNotFoundEx("Vehocle id is invalid Please select a right vehicle or select vehicle which is availble ");
            }
        
       } catch (Exception e) {
            throw new BookingNotFoundEx("Error creating booking: " + e.getMessage());
       }
    }

    public List<BookingDetailUser> getBookedVehicles(String email) {
        User user = userRepo.findByEmail(email);
        if(user==null){
            throw new UserNotFoundEx("User with email "+ email + " is not exist" );
        }
        List<BookingDetail> bookings = user.getBookingDetails();
        return bookings.stream()
                .map(book -> modelMapper.map(book , BookingDetailUser.class))
                .toList();
    }

    public BookingDetailUser cancelBooking(Long id, String email) {

        return bookingService.cancelBooking(id,getUserByEmail(email).getId());
    }

    public void deleteUser(Long userId) {
        bookingService.deleteUserBookings(userId);
        userRepo.deleteById(userId);
    }
}
