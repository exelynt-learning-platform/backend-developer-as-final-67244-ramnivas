package com.vehicleBooking.Controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vehicleBooking.DTOs.LoginREsponseDto;
import com.vehicleBooking.DTOs.UserRegisterDto;
import com.vehicleBooking.DTOs.UserloginDto;
import com.vehicleBooking.Models.User;
import com.vehicleBooking.Service.JwtService;
import com.vehicleBooking.Service.UserService;
import lombok.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController 
@RequestMapping()
public class AuthController {
    
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;

    public AuthController(UserService userService, AuthenticationManager authenticationManager, UserDetailsService userDetailsService, JwtService jwtService) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtService = jwtService;
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUser(@NonNull  @RequestBody UserRegisterDto userRegisterDto) {
        User user = userService.registerUser(userRegisterDto);
        if (user != null) {
            return ResponseEntity.ok("User registered successfully");
        } else {
            return ResponseEntity.badRequest().body("User registration failed User already exist");
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> loginUser(@RequestBody UserloginDto request) {
        try {
                authenticate(request.getEmail(), request.getPassword());
                final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
                final String jwt = jwtService.generateToken(userDetails.getUsername());

                ResponseCookie cookie = ResponseCookie.from("jwt", jwt)
                        .httpOnly(true)
                        .path("/")
                        .maxAge(10 * 60 * 60)
                        .sameSite("strict")
                        .build();

                return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                        .body(new LoginREsponseDto(userDetails.getUsername(), userDetails.getAuthorities().toString(),jwt));

        } catch (BadCredentialsException e) {
                return ResponseEntity.status(401).body("Invalid credentials");
        }

    }

    private void authenticate(String email, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        
    }
        
}
