package com.vehicleBooking.DTOs;

import java.util.Collection;
import java.util.List;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.vehicleBooking.Models.User;

import lombok.Data;
@Data 
public class UserInfoDetail implements UserDetails {

    private String username;
    private String password;
    private List<GrantedAuthority> authorities;

    public UserInfoDetail(User user) {
        this.username = user.getEmail();
        this.password = user.getPassword();
        this.authorities = List.of(new SimpleGrantedAuthority(user.getRole()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public @Nullable String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
       return username;
    }
    
}
