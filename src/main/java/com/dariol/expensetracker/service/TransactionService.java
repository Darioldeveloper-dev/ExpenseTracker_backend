package com.dariol.expensetracker.service;

import com.dariol.expensetracker.dto.TransactionRequest;
import com.dariol.expensetracker.dto.TransactionResponse;
import com.dariol.expensetracker.exception.ResourceNotFoundException;
import com.dariol.expensetracker.model.Category;
import com.dariol.expensetracker.model.Transaction;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.model.User;
import com.dariol.expensetracker.repository.CategoryRepository;
import com.dariol.expensetracker.repository.TransactionRepository;
import com.dariol.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    private TransactionResponse toResponse(Transaction t) {
        return new TransactionResponse(
                t.getId(),
                t.getAmount(),
                t.getType(),
                t.getDescription(),
                t.getDate(),
                t.getCategory().getId(),
                t.getCategory().getName(),
                t.getCreatedAt()
        );
    }

    public TransactionResponse createTransaction(TransactionRequest request) {
        User owner = getCurrentUser();

        Category category = categoryRepository.findByIdAndOwner(request.getCategoryId(), owner)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Transaction transaction = new Transaction();
        transaction.setAmount(request.getAmount());
        transaction.setType(request.getType());
        transaction.setDescription(request.getDescription());
        transaction.setDate(request.getDate());
        transaction.setCategory(category);
        transaction.setOwner(owner);

        Transaction saved = transactionRepository.save(transaction);

        return toResponse(saved);
    }

    public List<TransactionResponse> getTransactions() {
        User owner = getCurrentUser();

        return transactionRepository.findByOwnerOrderByDateDesc(owner).stream()
                .map(this::toResponse)
                .toList();
    }

    public BigDecimal getBalance() {
        User owner = getCurrentUser();

        BigDecimal totalIncome = transactionRepository.sumByOwnerAndType(owner, TransactionType.INCOME);
        BigDecimal totalExpense = transactionRepository.sumByOwnerAndType(owner, TransactionType.EXPENSE);

        return totalIncome.subtract(totalExpense);
    }
}
