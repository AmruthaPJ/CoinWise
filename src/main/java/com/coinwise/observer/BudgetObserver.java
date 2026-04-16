package com.coinwise.observer;

import com.coinwise.model.User;

public interface BudgetObserver {
    void update(User user, String msg);
}
