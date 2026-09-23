package com.vehicleBooking.Config;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.vehicleBooking.Service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component 
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
   
    public JwtFilter(JwtService jwtService,UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
                String authorizationHeader = request.getHeader("Authorization");
                String token = null;
                String email = null;

                if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                    token = authorizationHeader.substring(7);
                    email = jwtService.extractUsername(token);
                }

                if(token==null){
                    Cookie[] cookie = request.getCookies();
                    if(cookie != null){
                        for(Cookie ck : cookie){
                            if(ck.getName().equals("jwt")){
                                token = ck.getValue();
                                email = jwtService.extractUsername(token);
                                break;
                            }
                        }
                    } 
                }

                if(email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    var userDetails = userDetailsService.loadUserByUsername(email);
                    if(Boolean.TRUE.equals(jwtService.validateToken(token, userDetails.getUsername()))) {
                         UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }
                filterChain.doFilter(request, response);
    }

                
}
