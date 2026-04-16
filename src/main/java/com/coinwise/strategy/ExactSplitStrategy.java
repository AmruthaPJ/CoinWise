package com.coinwise.strategy;

import com.coinwise.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("EXACT")
public class ExactSplitStrategy implements SplitStrategy {
    @Override
    public Map<User, Double> calculate(double total, List<User> users, List<Double> params) {
        if (params == null || params.size() != users.size()) {
            throw new IllegalArgumentException("Exact amounts must match the number of users.");
        }

        double sum = params.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - total) > 0.01) {
            throw new IllegalArgumentException("Sum of exact amounts must match the total amount.");
        }

        Map<User, Double> splits = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            splits.put(users.get(i), params.get(i));
        }
        return splits;
    }
}
