package com.coinwise.strategy;

import com.coinwise.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("EQUAL")
public class EqualSplitStrategy implements SplitStrategy {
    @Override
    public Map<User, Double> calculate(double total, List<User> users, List<Double> params) {
        Map<User, Double> splits = new HashMap<>();
        double splitAmount = Math.round((total / users.size()) * 100.0) / 100.0;
        
        for (User user : users) {
            splits.put(user, splitAmount);
        }
        return splits;
    }
}
