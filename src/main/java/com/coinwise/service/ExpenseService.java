package com.coinwise.service;

import com.coinwise.dto.ExpenseDTO;
import com.coinwise.model.*;
import com.coinwise.repository.ExpenseRepository;
import com.coinwise.repository.GroupRepository;
import com.coinwise.repository.UserRepository;
import com.coinwise.strategy.SplitStrategy;
import com.coinwise.factory.ExpenseFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final BudgetManager budgetManager;
    private final Map<String, SplitStrategy> strategies;
    private final ExpenseFactory expenseFactory;

    public ExpenseService(ExpenseRepository expenseRepository, GroupRepository groupRepository, 
                          UserRepository userRepository, BudgetManager budgetManager, 
                          Map<String, SplitStrategy> strategies, ExpenseFactory expenseFactory) {
        this.expenseRepository = expenseRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.budgetManager = budgetManager;
        this.strategies = strategies;
        this.expenseFactory = expenseFactory;
    }

    @Transactional
    public void processExpense(ExpenseDTO dto, String payerUsername) {
        // 1. Validate Group and User
        User payer = userRepository.findByUsername(payerUsername)
                .orElseThrow(() -> new IllegalArgumentException("Payer not found"));
        Group group = null;
        List<User> participants = new java.util.ArrayList<>();
        
        if (dto.getSplitType() == com.coinwise.enums.SplitType.PERSONAL) {
            participants.add(payer);
        } else {
            group = groupRepository.findById(dto.getGroupId())
                    .orElseThrow(() -> new IllegalArgumentException("Group not found"));
                    
            if (dto.getUserIds() == null || dto.getUserIds().isEmpty()) {
                participants.addAll(group.getMembers());
            } else {
                for (String uid : dto.getUserIds()) {
                    userRepository.findById(uid).ifPresent(participants::add);
                }
            }
        }

        if (participants.isEmpty()) {
            throw new IllegalArgumentException("No valid participants found");
        }

        // 2. Select Strategy
        SplitStrategy strategy = strategies.get(dto.getSplitType().name());
        if (strategy == null) {
            throw new IllegalArgumentException("Unsupported Split Type");
        }

        // 3. Calculate Splits
        Map<User, Double> calculatedSplits = strategy.calculate(dto.getTotalAmount(), participants, dto.getSplitParams());

        // 4. Create and Save Expense via Factory Pattern
        Expense expense = expenseFactory.createExpense(
                group,
                dto.getDescription(),
                dto.getTotalAmount(),
                payer,
                dto.getSplitType()
        );

        for (Map.Entry<User, Double> entry : calculatedSplits.entrySet()) {
            User pUser = entry.getKey();
            Double amountOwed = entry.getValue();
            
            ExpenseSplit split = ExpenseSplit.builder()
                    .user(pUser)
                    .amountOwed(amountOwed)
                    .isPaid(pUser.getId().equals(payer.getId())) // Payer's own split is considered paid
                    .build();
            expense.addSplit(split);

            // Update Budget for everyone involved based on their exact calculated split
            if (amountOwed > 0) {
                budgetManager.checkBudget(pUser, amountOwed);
            }
        }

        expenseRepository.save(expense);
    }
}
