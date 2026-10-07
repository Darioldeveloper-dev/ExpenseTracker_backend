package com.dariol.expensetracker.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class RegisterResponse {
    private Long id;
    private String fullName;
    private String email;
    private LocalDateTime createdAt;
}
