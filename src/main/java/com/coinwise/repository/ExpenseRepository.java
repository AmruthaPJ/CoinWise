package com.coinwise.repository;

import com.coinwise.model.Expense;
import com.coinwise.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    List<Expense> findByGroupOrderByCreatedAtDesc(Group group);
    List<Expense> findByPaidByAndGroupIsNullOrderByCreatedAtDesc(com.coinwise.model.User paidBy);
}
