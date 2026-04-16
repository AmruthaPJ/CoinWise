-- Clear tables (useful for H2 auto restarts)
DELETE FROM notifications;
DELETE FROM settlements;
DELETE FROM expense_splits;
DELETE FROM expenses;
DELETE FROM budgets;
DELETE FROM group_members;
DELETE FROM groups_table;
DELETE FROM users;

-- Seed Users (Passwords are 'password123' BCrypt hashed)
INSERT INTO users (id, username, password, name, email) VALUES 
('user-uuid-1', 'alice', '$2a$10$EblZqNptyYvcLm/VwDCVAuIssG/6eR/Yxw2k9k9mC.l5J5Z6RtjWq', 'Alice Smith', 'alice@coinwise.com'),
('user-uuid-2', 'bob', '$2a$10$EblZqNptyYvcLm/VwDCVAuIssG/6eR/Yxw2k9k9mC.l5J5Z6RtjWq', 'Bob Johnson', 'bob@coinwise.com'),
('user-uuid-3', 'charlie', '$2a$10$EblZqNptyYvcLm/VwDCVAuIssG/6eR/Yxw2k9k9mC.l5J5Z6RtjWq', 'Charlie Davis', 'charlie@coinwise.com');

-- Seed Group
INSERT INTO groups_table (id, name, created_by) VALUES ('group-uuid-1', 'Viva Project Group', 'user-uuid-1');

-- Seed Group Members
INSERT INTO group_members (group_id, user_id) VALUES 
('group-uuid-1', 'user-uuid-1'),
('group-uuid-1', 'user-uuid-2'),
('group-uuid-1', 'user-uuid-3');

-- Seed Budgets
INSERT INTO budgets (id, user_id, limit_amount, current_spent, is_exceeded) VALUES 
('budget-uuid-1', 'user-uuid-1', 5000.0, 1000.0, FALSE),
('budget-uuid-2', 'user-uuid-2', 4000.0, 500.0, FALSE),
('budget-uuid-3', 'user-uuid-3', 3000.0, 500.0, FALSE);

-- Seed Expenses
INSERT INTO expenses (id, group_id, description, total_amount, paid_by, split_type, status) VALUES 
('exp-uuid-1', 'group-uuid-1', 'Dinner at Taj', 1500.00, 'user-uuid-1', 'EQUAL', 'UNPAID'),
('exp-uuid-2', 'group-uuid-1', 'Uber ride', 500.00, 'user-uuid-2', 'EQUAL', 'UNPAID');

-- Seed Expense Splits
INSERT INTO expense_splits (id, expense_id, user_id, amount_owed, is_paid) VALUES 
-- Alice paid 1500 (Alice owes 500, Bob owes 500, Charlie owes 500)
('split-uuid-1', 'exp-uuid-1', 'user-uuid-1', 500.00, TRUE), -- Alice's share
('split-uuid-2', 'exp-uuid-1', 'user-uuid-2', 500.00, FALSE),
('split-uuid-3', 'exp-uuid-1', 'user-uuid-3', 500.00, FALSE),

-- Bob paid 500 (Alice owes 166.67, Bob owes 166.66, Charlie owes 166.67)
('split-uuid-4', 'exp-uuid-2', 'user-uuid-1', 166.67, FALSE),
('split-uuid-5', 'exp-uuid-2', 'user-uuid-2', 166.66, TRUE), -- Bob's share
('split-uuid-6', 'exp-uuid-2', 'user-uuid-3', 166.67, FALSE);
