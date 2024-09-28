CREATE TABLE IF NOT EXISTS `user_and_phone` (
    `id` bigint NOT NULL,
    `email` varchar(255) DEFAULT NULL,
    `phone` varchar(255) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `invoice_person_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_invoice_user_id` (`invoice_person_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_up_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_up_invoice_user` FOREIGN KEY (`invoice_person_id`) REFERENCES `user` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;