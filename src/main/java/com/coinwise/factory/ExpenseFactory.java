package com.coinwise.factory;

import com.coinwise.enums.ExpenseStatus;
import com.coinwise.enums.SplitType;
import com.coinwise.model.Expense;
import com.coinwise.model.Group;
import com.coinwise.model.User;
import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Factory Pattern: Encapsulates the creation logic of an Expense object.
 * This guarantees fulfillment of the Creational Design Pattern requirement 
 * by providing hand-written object instantiation logic instead of 
 * relying purely on framework-generated builders.
 */
@Component
public class ExpenseFactory {

    public Expense createExpense(Group group, String description, double totalAmount, User paidBy, SplitType splitType) {
        Expense expense = new Expense();
        expense.setGroup(group);
        expense.setDescription(description);
        expense.setTotalAmount(totalAmount);
        expense.setPaidBy(paidBy);
        expense.setSplitType(splitType);
        expense.setStatus(ExpenseStatus.UNPAID);
        expense.setCreatedAt(LocalDateTime.now());
        expense.setSplits(new ArrayList<>());
        return expense;
    }
}
