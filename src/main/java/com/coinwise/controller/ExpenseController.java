package com.coinwise.controller;

import com.coinwise.dto.ExpenseDTO;
import com.coinwise.model.Expense;
import com.coinwise.model.Group;
import com.coinwise.model.User;
import com.coinwise.repository.ExpenseRepository;
import com.coinwise.repository.GroupRepository;
import com.coinwise.repository.UserRepository;
import com.coinwise.service.ExpenseService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;
    private final ExpenseRepository expenseRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public ExpenseController(ExpenseService expenseService, ExpenseRepository expenseRepository, GroupRepository groupRepository, UserRepository userRepository) {
        this.expenseService = expenseService;
        this.expenseRepository = expenseRepository;
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String listExpenses(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        List<Group> userGroups = groupRepository.findUserGroups(user);
        List<Expense> allExpenses = new ArrayList<>();
        for (Group g : userGroups) {
            allExpenses.addAll(expenseRepository.findByGroupOrderByCreatedAtDesc(g));
        }
        
        allExpenses.sort((e1, e2) -> {
            if (e1.getCreatedAt() == null && e2.getCreatedAt() == null) return 0;
            if (e1.getCreatedAt() == null) return 1;
            if (e2.getCreatedAt() == null) return -1;
            return e2.getCreatedAt().compareTo(e1.getCreatedAt());
        });
        model.addAttribute("expenses", allExpenses);
        return "expenses/list";
    }

    @PostMapping("/add")
    public String addExpense(@ModelAttribute ExpenseDTO dto, Authentication auth, RedirectAttributes redirectAttributes) {
        try {
            expenseService.processExpense(dto, auth.getName());
            redirectAttributes.addFlashAttribute("successMessage", 
                "✅ Expense added successfully via " + dto.getSplitType() + " matching.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "❌ Error: " + e.getMessage());
        }
        return "redirect:/dashboard";
    }
}
