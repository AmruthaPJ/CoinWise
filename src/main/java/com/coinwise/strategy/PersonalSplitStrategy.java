package com.coinwise.strategy;

import com.coinwise.model.User;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component("PERSONAL")
public class PersonalSplitStrategy implements SplitStrategy {
    
    @Override
    public Map<User, Double> calculate(double totalAmount, List<User> users, List<Double> params) {
        if (users == null || users.size() != 1) {
            throw new IllegalArgumentException("Personal expenses must be tied exactly to the sole tracking User.");
        }

        Map<User, Double> splits = new HashMap<>();
        splits.put(users.get(0), totalAmount);
        
        return splits;
    }
}
