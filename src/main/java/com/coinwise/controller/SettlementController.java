package com.coinwise.controller;

import com.coinwise.model.User;
import com.coinwise.repository.UserRepository;
import com.coinwise.service.SettlementService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settlements")
public class SettlementController {

    private final SettlementService settlementService;
    private final UserRepository userRepository;

    public SettlementController(SettlementService settlementService, UserRepository userRepository) {
        this.settlementService = settlementService;
        this.userRepository = userRepository;
    }

    @PostMapping("/pay")
    public String settleExpense(@RequestParam String expenseId, 
                                @RequestParam String payeeId,
                                @RequestParam double amount,
                                @RequestParam String groupId,
                                Authentication auth,
                                RedirectAttributes redirectAttributes) {
        try {
            User payer = userRepository.findByUsername(auth.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
            settlementService.recordSettlement(expenseId, payer.getId(), payeeId, amount);
            redirectAttributes.addFlashAttribute("successMessage", "✅ Successfully settled ₹" + amount + "!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Error settling: " + e.getMessage());
        }
        return "redirect:/groups/" + groupId;
    }
}
