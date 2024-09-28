CREATE TABLE IF NOT EXISTS `user_and_role` (
    `id` bigint NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `role_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_role_id` (`role_id`),
    KEY `idx_user_id`(`user_id`),
    CONSTRAINT  `fk_user_role_user`FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT   `fk_user_role_role`FOREIGN KEY (`role_id`) REFERENCES `role` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;