CREATE TABLE IF NOT EXISTS `role` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `created_at` datetime(6) DEFAULT NULL,
    `description` varchar(255) NOT NULL,
    `name` enum('ADMIN','DRIVER','KATHY','SUPER_ADMIN','USER','USER_KATHY') NOT NULL,
    `updated_at` datetime(6) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`),
    UNIQUE KEY `unique_username` (`name`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;