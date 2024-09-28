CREATE TABLE IF NOT EXISTS `wage_payment` (
    `id` bigint NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `date_from_id` bigint DEFAULT NULL,
    `date_to_id` bigint DEFAULT NULL,
    `invoice_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_date_from_id` (`date_from_id`),
    KEY `idx_date_to_id` (`date_to_id`),
    KEY `idx_invoice_id` (`invoice_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_wp_invoice` FOREIGN KEY (`invoice_id`) REFERENCES `invoice` (`id`),
    CONSTRAINT `fk_wp_date_from` FOREIGN KEY (`date_from_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_wp_date_to` FOREIGN KEY (`date_to_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_wp_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
