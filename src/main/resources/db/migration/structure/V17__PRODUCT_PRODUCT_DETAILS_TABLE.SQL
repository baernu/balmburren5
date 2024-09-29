CREATE TABLE IF NOT EXISTS `product_bind_product_details` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `is_checked` bit(1) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `end_date_id` bigint DEFAULT NULL,
    `product_id` bigint DEFAULT NULL,
    `product_details_id` bigint DEFAULT NULL,
    `start_date_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_end_date_id`  (`end_date_id`),
    KEY `idx_product_id`  (`product_id`),
    KEY `idx_product_details_id`  (`product_details_id`),
    KEY `idx_start_date_id`  (`start_date_id`),
    CONSTRAINT `fk_pbpd_details` FOREIGN KEY (`product_details_id`) REFERENCES `product_info` (`id`),
    CONSTRAINT `fk_pbpd_end_date` FOREIGN KEY (`end_date_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_pbpd_product` FOREIGN KEY (`product_id`) REFERENCES `product` (`id`),
    CONSTRAINT `fk_pbpd_start_date` FOREIGN KEY (`start_date_id`) REFERENCES `dates` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;