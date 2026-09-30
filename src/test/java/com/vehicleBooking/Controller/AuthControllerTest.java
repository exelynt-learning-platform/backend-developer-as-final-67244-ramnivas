package com.vehicleBooking.Controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.vehicleBooking.DTOs.LoginREsponseDto;
import com.vehicleBooking.DTOs.UserRegisterDto;
import com.vehicleBooking.DTOs.UserloginDto;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Service.JwtService;
import com.vehicleBooking.Service.UserService;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void registerUser_shouldReturnSuccessResponse_whenUserIsCreated() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setName("John");
        dto.setEmail("john@example.com");
        dto.setPassword("password123");
        dto.setPhoneNumber("9876543210");

        User savedUser = new User();
        savedUser.setEmail(dto.getEmail());
        savedUser.setName(dto.getName());

        when(userService.registerUser(dto)).thenReturn(savedUser);

        ResponseEntity<?> response = authController.registerUser(dto);

        assertEquals(200, response.getStatusCode().value());
        assertEquals("User registered successfully", response.getBody());
    }

    @Test
    void registerUser_shouldReturnBadRequest_whenUserAlreadyExists() {
        UserRegisterDto dto = new UserRegisterDto();
        dto.setEmail("existing@example.com");
        dto.setPassword("password123");

        when(userService.registerUser(dto)).thenReturn(null);

        ResponseEntity<?> response = authController.registerUser(dto);

        assertEquals(400, response.getStatusCode().value());
        assertEquals("User registration failed User already exist", response.getBody());
    }

    @Test
    void loginUser_shouldReturnJwtCookieAndLoginResponse_whenCredentialsAreValid() {
        UserloginDto request = new UserloginDto();
        request.setEmail("john@example.com");
        request.setPassword("password123");

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withUsername("john@example.com")
                .password("encodedPassword")
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(mock(Authentication.class));
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(userDetails);
        when(jwtService.generateToken("john@example.com")).thenReturn("jwt-token-123");

        ResponseEntity<?> response = authController.loginUser(request);

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
        assertTrue(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE).contains("jwt=jwt-token-123"));

        LoginREsponseDto body = (LoginREsponseDto) response.getBody();
        assertNotNull(body);
        assertEquals("john@example.com", body.getEmail());
        assertEquals("[ROLE_USER]", body.getRole());
        assertEquals("jwt-token-123", body.getJwt());
    }

    @Test
    void loginUser_shouldReturnUnauthorized_whenCredentialsAreInvalid() {
        UserloginDto request = new UserloginDto();
        request.setEmail("john@example.com");
        request.setPassword("wrong-password");

        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("bad credentials"));

        ResponseEntity<?> response = authController.loginUser(request);

        assertEquals(401, response.getStatusCode().value());
        assertEquals("Invalid credentials", response.getBody());
    }
}
