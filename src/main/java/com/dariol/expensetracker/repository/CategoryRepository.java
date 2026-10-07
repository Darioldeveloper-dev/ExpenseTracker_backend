package com.dariol.expensetracker.repository;

import com.dariol.expensetracker.model.Category;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findByOwner(User owner);

    List<Category> findByOwnerAndType(User owner, TransactionType type);

    Optional<Category> findByIdAndOwner(Long id, User owner);
}
