package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier; // Import Qualifier
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.Entity.Users;
import com.example.demo.configuration.dto.LoginDto;
import com.example.demo.repos.UserReposi;
import com.example.demo.utility.JwtToken;

@RestController
@RequestMapping("/auth")
public class AuthControll {

    @Autowired
    UserReposi userepo;

    @Autowired
    PasswordEncoder passencode;

    @Autowired
    AuthenticationManager authenticatemanager;

    @Autowired
    JwtToken jwt;

    // Use @Qualifier to specify which UserDetailsService bean to inject
    @Autowired
    @Qualifier("customUserDetailService") // Or "userdetailsservice" if that's the one you need
    private UserDetailsService userDetailsService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users user) {
        if (userepo.existsByname(user.getName())) {
            return new ResponseEntity<>("user already exists", HttpStatus.BAD_REQUEST);
        }

        user.setPassword(passencode.encode(user.getPassword()));
        userepo.save(user);

        return new ResponseEntity<>("registered successfully", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDto loginreq) {
        try {
            Authentication authentication = authenticatemanager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginreq.getName(), loginreq.getPassword()));

            SecurityContextHolder.getContext().setAuthentication(authentication);

            UserDetails userDetails = userDetailsService.loadUserByUsername(loginreq.getName());

            return jwt.generateToken(userDetails);

        } catch (BadCredentialsException ex) {
            return "Unauthorized: Invalid username or password";
        } catch (Exception ex) {
            return "Login failed: " + ex.getMessage();
        }
    }
}