package com.dariol.expensetracker.repository;

import com.dariol.expensetracker.model.Transaction;
import com.dariol.expensetracker.model.TransactionType;
import com.dariol.expensetracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByOwnerOrderByDateDesc(User owner);

    Optional<Transaction> findByIdAndOwner(Long id, User owner);

    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.owner = :owner AND t.type = :type")
    BigDecimal sumByOwnerAndType(@Param("owner") User owner, @Param("type") TransactionType type);
}