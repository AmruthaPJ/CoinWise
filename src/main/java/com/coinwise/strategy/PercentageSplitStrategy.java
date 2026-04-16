package com.coinwise.strategy;

import com.coinwise.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("PERCENTAGE")
public class PercentageSplitStrategy implements SplitStrategy {
    @Override
    public Map<User, Double> calculate(double total, List<User> users, List<Double> params) {
        if (params == null || params.size() != users.size()) {
            throw new IllegalArgumentException("Percentages must match the number of users.");
        }

        double sum = params.stream().mapToDouble(Double::doubleValue).sum();
        if (Math.abs(sum - 100.0) > 0.01) {
            throw new IllegalArgumentException("Total percentage must add up to 100%.");
        }

        Map<User, Double> splits = new HashMap<>();
        for (int i = 0; i < users.size(); i++) {
            double amount = Math.round((total * params.get(i) / 100.0) * 100.0) / 100.0;
            splits.put(users.get(i), amount);
        }
        return splits;
    }
}
