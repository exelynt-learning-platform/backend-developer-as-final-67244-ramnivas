package com.vehicleBooking.Service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.vehicleBooking.DTOs.UserInfoDetail;
import com.vehicleBooking.DTOs.UserRegisterDto;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Repository.UserRepo;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoderService;

    @Mock
    private BookingService bookingService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldSaveUser_whenEmailDoesNotExist() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setName("Alice");
        dto.setEmail("alice@example.com");
        dto.setPassword("secret123");
        dto.setPhoneNumber("1112223333");

        when(userRepo.findByEmail("alice@example.com")).thenReturn(null);
        when(passwordEncoderService.encode("secret123")).thenReturn("encodedSecret");

        User savedUser = new User();
        savedUser.setName("Alice");
        savedUser.setEmail("alice@example.com");
        savedUser.setPassword("encodedSecret");
        savedUser.setPhoneNumber("1112223333");
        savedUser.setRole("ROLE_USER");
        when(userRepo.save(any(User.class))).thenReturn(savedUser);

        User result = userService.registerUser(dto);

        assertNotNull(result);
        assertEquals("ROLE_USER", result.getRole());
        assertEquals("encodedSecret", result.getPassword());
        verify(userRepo).save(any(User.class));
    }

    @Test
    void registerUser_shouldReturnNull_whenEmailAlreadyExists() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setEmail("existing@example.com");
        dto.setPassword("secret123");

        User existingUser = new User();
        existingUser.setEmail("existing@example.com");
        when(userRepo.findByEmail("existing@example.com")).thenReturn(existingUser);

        User result = userService.registerUser(dto);

        assertNull(result);
    }

    @Test
    void loadUserByUsername_shouldReturnAuthenticatedUserDetails_whenUserExists() {
        User user = new User();
        user.setEmail("bob@example.com");
        user.setPassword("encodedPassword");
        user.setRole("ROLE_USER");

        when(userRepo.findByEmail("bob@example.com")).thenReturn(user);

        UserDetails result = userService.loadUserByUsername("bob@example.com");

        assertNotNull(result);
        assertEquals("bob@example.com", result.getUsername());
        assertEquals("encodedPassword", result.getPassword());
        assertEquals("ROLE_USER", result.getAuthorities().iterator().next().getAuthority());
        assertTrue(result instanceof UserInfoDetail);
    }

    @Test
    void loadUserByUsername_shouldThrowException_whenUserDoesNotExist() {
        when(userRepo.findByEmail("missing@example.com")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername("missing@example.com"));
    }
}
