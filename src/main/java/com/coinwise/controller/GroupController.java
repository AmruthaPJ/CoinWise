package com.coinwise.controller;

import com.coinwise.enums.NotificationType;
import com.coinwise.model.Group;
import com.coinwise.model.Notification;
import com.coinwise.model.User;
import com.coinwise.repository.ExpenseRepository;
import com.coinwise.repository.GroupRepository;
import com.coinwise.repository.NotificationRepository;
import com.coinwise.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/groups")
public class GroupController {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;
    private final NotificationRepository notificationRepository;
    private final ExpenseRepository expenseRepository;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository, NotificationRepository notificationRepository, ExpenseRepository expenseRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
        this.expenseRepository = expenseRepository;
    }

    @GetMapping
    public String listGroups(Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        model.addAttribute("groups", groupRepository.findUserGroups(user));
        return "groups/list";
    }

    @PostMapping("/create")
    public String createGroup(@RequestParam String name, 
                              @RequestParam(required = false) String participants, 
                              Authentication auth, 
                              RedirectAttributes redirectAttributes) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        Group group = Group.builder()
                .name(name)
                .createdBy(user)
                .createdAt(LocalDateTime.now())
                .build();
        
        group.addMember(user); // the creator is always a member
        
        if (participants != null && !participants.isBlank()) {
            String[] split = participants.split(",");
            for (String username : split) {
                userRepository.findByUsername(username.trim()).ifPresent(p -> {
                    if (!p.getId().equals(user.getId())) {
                        group.addMember(p);
                        
                        Notification notif = Notification.builder()
                                .user(p)
                                .message(user.getName() + " added you to the group '" + name + "'!")
                                .type(NotificationType.SETTLEMENT_DONE)
                                .isRead(false)
                                .createdAt(LocalDateTime.now())
                                .build();
                        notificationRepository.save(notif);
                    }
                });
            }
        }
        
        groupRepository.save(group);
        redirectAttributes.addFlashAttribute("successMessage", "✅ Group '" + name + "' created successfully with " + group.getMembers().size() + " members!");
        return "redirect:/groups";
    }

    @GetMapping("/{id}")
    public String groupDetails(@PathVariable String id, Model model, Authentication auth) {
        User user = userRepository.findByUsername(auth.getName()).orElseThrow();
        Group group = groupRepository.findById(id).orElseThrow();
        model.addAttribute("group", group);
        model.addAttribute("expenses", expenseRepository.findByGroupOrderByCreatedAtDesc(group));
        model.addAttribute("currentUser", user);
        return "groups/detail";
    }
}
