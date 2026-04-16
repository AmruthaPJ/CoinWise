package com.coinwise.dto;

import com.coinwise.model.Budget;
import com.coinwise.model.Expense;
import com.coinwise.model.Group;
import com.coinwise.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class DashboardData {
    private User user;
    private Budget budget;
    private List<Group> groups;
    private List<Expense> recentExpenses;
}
