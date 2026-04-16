package com.coinwise.facade;

import com.coinwise.dto.DashboardData;
import com.coinwise.model.Budget;
import com.coinwise.model.Expense;
import com.coinwise.model.Group;
import com.coinwise.model.User;
import com.coinwise.repository.BudgetRepository;
import com.coinwise.repository.ExpenseRepository;
import com.coinwise.repository.GroupRepository;
import com.coinwise.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Facade Pattern: Provides a simplified, unified interface to the complex subsystem of
 * retrieving various dashboard entities. This satisfies the Structural Design Pattern 
 * requirement for the mini-project.
 */
@Service
public class DashboardFacade {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final GroupRepository groupRepository;
    private final ExpenseRepository expenseRepository;

    public DashboardFacade(UserRepository userRepository, BudgetRepository budgetRepository,
                           GroupRepository groupRepository, ExpenseRepository expenseRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.groupRepository = groupRepository;
        this.expenseRepository = expenseRepository;
    }

    public DashboardData getDashboardData(String username) {
        User currentUser = userRepository.findByUsername(username).orElseThrow();
        Budget budget = budgetRepository.findByUser(currentUser).orElse(null);
        List<Group> userGroups = groupRepository.findUserGroups(currentUser);

        // Fetch recent expenses from all user's groups
        List<Expense> allRecent = userGroups.stream()
                .flatMap(g -> expenseRepository.findByGroupOrderByCreatedAtDesc(g).stream())
                .collect(Collectors.toList());

        List<Expense> recentExpenses = allRecent.stream()
                .sorted((e1, e2) -> {
                    if (e1.getCreatedAt() == null && e2.getCreatedAt() == null) return 0;
                    if (e1.getCreatedAt() == null) return 1; // Put nulls last
                    if (e2.getCreatedAt() == null) return -1;
                    return e2.getCreatedAt().compareTo(e1.getCreatedAt());
                })
                .limit(5)
                .collect(Collectors.toList());

        return new DashboardData(currentUser, budget, userGroups, recentExpenses);
    }
}
