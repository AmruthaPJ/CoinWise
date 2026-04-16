package com.coinwise.controller;

import com.coinwise.model.Budget;
import com.coinwise.model.User;
import com.coinwise.repository.BudgetRepository;
import com.coinwise.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/settings")
public class BudgetController {

    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;

    public BudgetController(UserRepository userRepository, BudgetRepository budgetRepository) {
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
    }

    @GetMapping
    public String settings(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        Budget budget = budgetRepository.findByUser(user).orElse(null);
        model.addAttribute("user", user);
        model.addAttribute("budget", budget != null ? budget.getLimitAmount() : 5000.0);
        return "settings";
    }

    @PostMapping("/update")
    public String updateSettings(@RequestParam String name, 
                                 @RequestParam String email, 
                                 @RequestParam Double budgetLimit, 
                                 Authentication auth, 
                                 RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        user.setName(name);
        user.setEmail(email);
        userRepository.save(user);

        Budget budget = budgetRepository.findByUser(user).orElse(new Budget());
        budget.setUser(user);
        budget.setLimitAmount(budgetLimit);
        budgetRepository.save(budget);

        redirectAttributes.addFlashAttribute("successMessage", "✅ Profile and Budget limits successfully updated!");
        return "redirect:/settings";
    }
}
