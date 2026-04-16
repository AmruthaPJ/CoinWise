package com.coinwise.controller;

import com.coinwise.dto.DashboardData;
import com.coinwise.enums.SplitType;
import com.coinwise.facade.DashboardFacade;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping({"/", "/dashboard"})
public class DashboardController {

    private final DashboardFacade dashboardFacade;

    public DashboardController(DashboardFacade dashboardFacade) {
        this.dashboardFacade = dashboardFacade;
    }

    @GetMapping
    public String index(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/auth/login";
        }

        String username = authentication.getName();
        // Uses the newly created Structural Design Pattern (Facade)
        DashboardData data = dashboardFacade.getDashboardData(username);

        model.addAttribute("user", data.getUser());
        model.addAttribute("budget", data.getBudget());
        model.addAttribute("recentExpenses", data.getRecentExpenses());
        model.addAttribute("groups", data.getGroups());
        model.addAttribute("splitTypes", SplitType.values());

        return "dashboard";
    }
}
