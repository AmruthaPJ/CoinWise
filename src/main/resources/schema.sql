CREATE TABLE IF NOT EXISTS users (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS groups_table (
    id VARCHAR(36) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    created_by VARCHAR(36),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (created_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS group_members (
    group_id VARCHAR(36),
    user_id VARCHAR(36),
    PRIMARY KEY (group_id, user_id),
    FOREIGN KEY (group_id) REFERENCES groups_table(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS expenses (
    id VARCHAR(36) PRIMARY KEY,
    group_id VARCHAR(36),
    description VARCHAR(255) NOT NULL,
    total_amount DECIMAL(10,2) NOT NULL,
    paid_by VARCHAR(36),
    split_type VARCHAR(20) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (group_id) REFERENCES groups_table(id),
    FOREIGN KEY (paid_by) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS expense_splits (
    id VARCHAR(36) PRIMARY KEY,
    expense_id VARCHAR(36),
    user_id VARCHAR(36),
    amount_owed DECIMAL(10,2) NOT NULL,
    percent DECIMAL(5,2),
    is_paid BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (expense_id) REFERENCES expenses(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS budgets (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36) UNIQUE,
    limit_amount DECIMAL(10,2) NOT NULL,
    current_spent DECIMAL(10,2) DEFAULT 0.0,
    is_exceeded BOOLEAN DEFAULT FALSE,
    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS settlements (
    id VARCHAR(36) PRIMARY KEY,
    settlement_id VARCHAR(36),
    expense_id VARCHAR(36),
    payer VARCHAR(36),
    payee VARCHAR(36),
    amount DECIMAL(10,2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (expense_id) REFERENCES expenses(id),
    FOREIGN KEY (payer) REFERENCES users(id),
    FOREIGN KEY (payee) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS notifications (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    message VARCHAR(255) NOT NULL,
    type VARCHAR(30) NOT NULL,
    is_read BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id)
);
