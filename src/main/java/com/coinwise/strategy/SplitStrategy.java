package com.coinwise.strategy;

import com.coinwise.model.User;
import java.util.List;
import java.util.Map;

public interface SplitStrategy {
    Map<User, Double> calculate(double total, List<User> users, List<Double> params);
}
