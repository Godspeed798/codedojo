CREATE TABLE IF NOT EXISTS app_user (
    id VARCHAR(36) PRIMARY KEY,
    username VARCHAR(50) UNIQUE NOT NULL,
    level INT DEFAULT 1,
    exp INT DEFAULT 0,
    coins INT DEFAULT 100,
    equipment VARCHAR(500),
    hints_today INT DEFAULT 3,
    achievements VARCHAR(500)
);

CREATE TABLE IF NOT EXISTS level (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    difficulty INT,
    is_free BOOLEAN DEFAULT TRUE,
    description TEXT,
    starter_code TEXT,
    solution TEXT,
    time_limit INT,
    exp_reward INT,
    coin_reward INT
);

CREATE TABLE IF NOT EXISTS progress (
    user_id VARCHAR(36),
    level_id INT,
    completed BOOLEAN DEFAULT FALSE,
    stars INT DEFAULT 0,
    best_time INT,
    PRIMARY KEY (user_id, level_id)
);

CREATE TABLE IF NOT EXISTS equipment (
    id INT PRIMARY KEY,
    name VARCHAR(100),
    type VARCHAR(50),
    price INT,
    effect VARCHAR(100),
    effect_value INT,
    rarity VARCHAR(20),
    icon VARCHAR(50),
    is_free BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS payment (
    id VARCHAR(36) PRIMARY KEY,
    user_id VARCHAR(36),
    transaction_id VARCHAR(100),
    amount INT,
    content VARCHAR(100),
    create_time TIMESTAMP,
    verified BOOLEAN DEFAULT FALSE
);

ALTER TABLE app_user ADD COLUMN unlocked_content TEXT;

TRUNCATE TABLE payment;