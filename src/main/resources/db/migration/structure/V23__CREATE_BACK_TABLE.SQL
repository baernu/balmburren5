CREATE TABLE IF NOT EXISTS `back` (
    `id` bigint NOT NULL,
    `quantity` int NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `date_id` bigint DEFAULT NULL,
    `deliver_people_id` bigint DEFAULT NULL,
    `product_bind_infos_id` bigint DEFAULT NULL,
     PRIMARY KEY (`id`),
    KEY `idx_date_id` (`date_id`),
    KEY `idx_user_id` (`deliver_people_id`),
    KEY `idx_product_info_id` (`product_bind_infos_id`),
    CONSTRAINT `fk_back_date` FOREIGN KEY (`date_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_back_user` FOREIGN KEY (`deliver_people_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_back_product_info` FOREIGN KEY (`product_bind_infos_id`) REFERENCES `product_bind_product_details` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;