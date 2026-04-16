package com.coinwise.service;

import com.coinwise.model.Budget;
import com.coinwise.model.User;
import com.coinwise.observer.BudgetObserver;
import com.coinwise.repository.BudgetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BudgetManager {

    private final List<BudgetObserver> observers;
    private final BudgetRepository budgetRepository;

    public BudgetManager(List<BudgetObserver> observers, BudgetRepository budgetRepository) {
        this.observers = observers;
        this.budgetRepository = budgetRepository;
    }

    @Transactional
    public void checkBudget(User user, double expenseAmount) {
        Budget budget = budgetRepository.findByUser(user).orElseGet(() -> 
            Budget.builder()
                .user(user)
                .limitAmount(5000.0) // Provide a standard default
                .currentSpent(0.0)
                .isExceeded(false)
                .build()
        );

        double newSpent = budget.getCurrentSpent() + expenseAmount;
        budget.setCurrentSpent(newSpent);

        if (newSpent > budget.getLimitAmount() && !budget.getIsExceeded()) {
            budget.setIsExceeded(true);
            notifyObservers(user, "Alert: You have exceeded your budget limit of ₹" + budget.getLimitAmount());
        } else if (newSpent <= budget.getLimitAmount()) {
            budget.setIsExceeded(false); // Reset if they somehow reduced expenses
        }

        budgetRepository.save(budget);
    }

    private void notifyObservers(User user, String msg) {
        for (BudgetObserver obj : observers) {
            obj.update(user, msg);
        }
    }
}
