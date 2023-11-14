package com.example.springboot.controller;

import com.example.springboot.DTO.AuthRequest;
import com.example.springboot.entity.Cart;
import com.example.springboot.entity.User;
import com.example.springboot.request.PasswordRequest;
import com.example.springboot.request.ResetPasswordRequest;
import com.example.springboot.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService userService;
    @Autowired
    private JWTService jwtService;
    private CartService cartService;
    @Autowired
    private AuthenticationManager authenticationManager;
    public AuthController(UserService userService,CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
    }


//Registration for both USER and ADMIN

    @PostMapping("/register")
    public ResponseEntity<?> saveUser(@RequestBody User user) {
        String email = user.getEmail().toLowerCase();
        user.setEmail(email);
        if(user.getRole()==null){
            user.setRole("ROLE_USER");

        }
        User savedUser = userService.saveUser(user);
        Long cartid = null;
        if (savedUser.getRole().equals("ROLE_USER")) {
            Cart cart = new Cart(user);
            Cart newcart = cartService.saveCart(cart);
            cartid = newcart.getId();
            return new ResponseEntity<>("Registration Successful!! Your cart ID is " + cartid, HttpStatus.CREATED);
        }
        else
        return new ResponseEntity<>("Registration Successful!!", HttpStatus.CREATED);
    }

//Token Generating for both USER and ADMIN

    @PostMapping("/authenticate")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("invalid user request !");
        }
    }

    //Forgot Password
    @PostMapping("/ForgotPassword")
    public String sendSms(@Valid @RequestBody PasswordRequest req) {
        String email= req.getEmail();
        userService.forgotPassword(email);
        return "Reset Token has been sent to your phone number.please generate new Password.";
    }

    //Reset New Password

    @PostMapping("/ResetPassword")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        User user = userService.findUserByResetToken(request.getResetToken());
        if (user == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reset token.");
        }

        // Proceed with password reset process
        userService.updatePasswordWithResetToken(request.getResetToken(), request.getNewPassword());

        return ResponseEntity.ok("Password reset successfully.");
    }

}
