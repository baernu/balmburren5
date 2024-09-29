CREATE TABLE IF NOT EXISTS `user_profile_order` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `first_week_order` int DEFAULT NULL,
    `second_week_order` int DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `product_bind_product_details_id` bigint DEFAULT NULL,
    `tour_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_upo_product_product_details_id` (`product_bind_product_details_id`),
    KEY `idx_upo_tour_id` (`tour_id`),
    KEY `idx_upo_user_id` (`user_id`),
    CONSTRAINT `fk_upo_tour` FOREIGN KEY (`tour_id`) REFERENCES `tour` (`id`),
    CONSTRAINT `fk_upo_product_product_info` FOREIGN KEY (`product_bind_product_details_id`) REFERENCES `product_bind_product_details` (`id`),
    CONSTRAINT `fk_upo_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;