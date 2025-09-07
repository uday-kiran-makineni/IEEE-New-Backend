package com.example.demo.dto;

import com.example.demo.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private UserDTO user;
    private String message;
    
    public LoginResponse(String token, UserDTO user) {
        this.token = token;
        this.user = user;
        this.message = "Login successful";
    }
}
