package com.dariol.expensetracker.service;

import com.dariol.expensetracker.model.Category;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.repository.CategoryRepository;
import java.util.List;
import com.dariol.expensetracker.dto.LoginRequest;
import com.dariol.expensetracker.dto.LoginResponse;
import com.dariol.expensetracker.dto.RegisterRequest;
import com.dariol.expensetracker.dto.RegisterResponse;
import com.dariol.expensetracker.exception.DuplicateEmailException;
import com.dariol.expensetracker.exception.InvalidCredentialsException;
import com.dariol.expensetracker.model.User;
import com.dariol.expensetracker.repository.UserRepository;
import com.dariol.expensetracker.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final CategoryRepository categoryRepository;

    public RegisterResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException(request.getEmail());
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        User saved = userRepository.save(user);
        seedDefaultCategories(saved);

        return new RegisterResponse(
                saved.getId(),
                saved.getFullName(),
                saved.getEmail(),
                saved.getCreatedAt()
        );
    }
    public LoginResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtUtil.generateToken(user.getEmail());

        return new LoginResponse(token, user.getId(), user.getFullName(), user.getEmail());
    }
    private void seedDefaultCategories(User owner) {
        List<Category> defaults = List.of(
                newCategory("Groceries", TransactionType.EXPENSE, owner),
                newCategory("Transport", TransactionType.EXPENSE, owner),
                newCategory("Rent", TransactionType.EXPENSE, owner),
                newCategory("Utilities", TransactionType.EXPENSE, owner),
                newCategory("Entertainment", TransactionType.EXPENSE, owner),
                newCategory("Salary", TransactionType.INCOME, owner),
                newCategory("Other Income", TransactionType.INCOME, owner)
        );
        categoryRepository.saveAll(defaults);
    }

    private Category newCategory(String name, TransactionType type, User owner) {
        Category category = new Category();
        category.setName(name);
        category.setType(type);
        category.setOwner(owner);
        return category;
    }
}
