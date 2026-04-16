package com.coinwise.repository;

import com.coinwise.model.Budget;
import com.coinwise.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BudgetRepository extends JpaRepository<Budget, String> {
    Optional<Budget> findByUser(User user);
}
