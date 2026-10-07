package com.dariol.expensetracker.controller;

import com.dariol.expensetracker.dto.CategoryRequest;
import com.dariol.expensetracker.dto.CategoryResponse;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse response = categoryService.createCategory(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getCategories(
            @RequestParam(required = false) TransactionType type
    ) {
        return ResponseEntity.ok(categoryService.getCategories(type));
    }
}
