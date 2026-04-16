package com.coinwise.dto;

import com.coinwise.enums.SplitType;
import lombok.Data;
import java.util.List;

@Data
public class ExpenseDTO {
    private String groupId;
    private String description;
    private Double totalAmount;
    private SplitType splitType;
    
    // Custom split parameters
    private List<String> userIds;
    private List<Double> splitParams; 
}
