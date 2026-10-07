package com.dariol.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String tokenType = "Bearer";
    private Long id;
    private String fullName;
    private String email;

    public LoginResponse(String token, Long id, String fullName, String email) {
        this.token = token;
        this.tokenType = "Bearer";
        this.id = id;
        this.fullName = fullName;
        this.email = email;
    }
}
