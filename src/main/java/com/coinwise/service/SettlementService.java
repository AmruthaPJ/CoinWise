package com.coinwise.service;

import com.coinwise.enums.ExpenseStatus;
import com.coinwise.enums.NotificationType;
import com.coinwise.enums.SettlementStatus;
import com.coinwise.model.*;
import com.coinwise.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class SettlementService {

    private final SettlementRepository settlementRepository;
    private final ExpenseRepository expenseRepository;
    private final NotificationRepository notificationRepository;

    public SettlementService(SettlementRepository settlementRepository, ExpenseRepository expenseRepository, NotificationRepository notificationRepository) {
        this.settlementRepository = settlementRepository;
        this.expenseRepository = expenseRepository;
        this.notificationRepository = notificationRepository;
    }

    @Transactional
    public void recordSettlement(String expenseId, String payerId, String payeeId, double amount) {
        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        // Find the split corresponding to the payer
        ExpenseSplit targetSplit = expense.getSplits().stream()
                .filter(s -> s.getUser().getId().equals(payerId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("User is not part of this expense"));

        targetSplit.setIsPaid(true);

        Settlement settlement = Settlement.builder()
                .settlementId(UUID.randomUUID().toString())
                .expense(expense)
                .payer(targetSplit.getUser())
                .payee(expense.getPaidBy()) // The one who originally paid the whole bill
                .amount(amount)
                .status(SettlementStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();

        settlementRepository.save(settlement);

        // Update overall expense status
        long unpaidCount = expense.getSplits().stream().filter(s -> !s.getIsPaid()).count();
        if (unpaidCount == 0) {
            expense.setStatus(ExpenseStatus.SETTLED);
        } else {
            expense.setStatus(ExpenseStatus.PARTIALLY_PAID);
        }
        expenseRepository.save(expense);

        // Notify payee
        Notification notif = Notification.builder()
                .user(expense.getPaidBy())
                .message(targetSplit.getUser().getName() + " just settled ₹" + amount + " for '" + expense.getDescription() + "'")
                .type(NotificationType.SETTLEMENT_DONE)
                .isRead(false)
                .createdAt(LocalDateTime.now())
                .build();
        notificationRepository.save(notif);
    }

    // A placeholder for the advanced net debt simplification algorithm requested for viva
    public void simplifyGroupDebts(String groupId) {
        // Algorithm:
        // 1. Calculate net balance per user (Total Owed vs Total Paid)
        // 2. Separate into Debtors and Creditors
        // 3. Match highest Debtor with highest Creditor to minimize transactions
        // Note: Implementation omitted for briefness, would process and return suggested Settlements
    }
}
