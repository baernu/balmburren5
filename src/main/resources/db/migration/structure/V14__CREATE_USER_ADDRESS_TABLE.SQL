CREATE TABLE IF NOT EXISTS `user_and_deliver_address` (
    `id` bigint NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `address_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
     PRIMARY KEY (`id`),
    KEY `idx_address_id` (`address_id`),
    KEY `idx_user_id`(`user_id`),
    CONSTRAINT `fk_user_address_address` FOREIGN KEY (`address_id`) REFERENCES `address` (`id`),
    CONSTRAINT `fk_user_address_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;