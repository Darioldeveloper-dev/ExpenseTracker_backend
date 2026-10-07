package com.dariol.expensetracker.dto;

import com.dariol.expensetracker.model.TransactionType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class TransactionResponse {
    private Long id;
    private BigDecimal amount;
    private TransactionType type;
    private String description;
    private LocalDate date;
    private Long categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
}
