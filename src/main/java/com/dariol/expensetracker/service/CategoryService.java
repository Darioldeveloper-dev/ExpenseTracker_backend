package com.dariol.expensetracker.service;

import com.dariol.expensetracker.dto.CategoryRequest;
import com.dariol.expensetracker.dto.CategoryResponse;
import com.dariol.expensetracker.model.Category;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.model.User;
import com.dariol.expensetracker.repository.CategoryRepository;
import com.dariol.expensetracker.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    private User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Authenticated user not found"));
    }

    public CategoryResponse createCategory(CategoryRequest request) {
        User owner = getCurrentUser();

        Category category = new Category();
        category.setName(request.getName());
        category.setType(request.getType());
        category.setOwner(owner);

        Category saved = categoryRepository.save(category);

        return new CategoryResponse(saved.getId(), saved.getName(), saved.getType());
    }

    public List<CategoryResponse> getCategories(TransactionType type) {
        User owner = getCurrentUser();

        List<Category> categories = (type != null)
                ? categoryRepository.findByOwnerAndType(owner, type)
                : categoryRepository.findByOwner(owner);

        return categories.stream()
                .map(c -> new CategoryResponse(c.getId(), c.getName(), c.getType()))
                .toList();
    }
}
