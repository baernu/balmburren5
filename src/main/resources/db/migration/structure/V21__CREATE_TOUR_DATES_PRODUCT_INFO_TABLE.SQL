CREATE TABLE IF NOT EXISTS `tour_bind_dates_productbindinfo` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `optlock` int NOT NULL DEFAULT '0',
    `dates_id` bigint DEFAULT NULL,
    `product_bind_infos_id` bigint DEFAULT NULL,
    `tour_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_date_id`  (`dates_id`),
    KEY `idx_product_info_id`  (`product_bind_infos_id`),
    KEY `idx_tour_id`  (`tour_id`),
    CONSTRAINT `fk_tbdpbi_product_info` FOREIGN KEY (`product_bind_infos_id`) REFERENCES `product_bind_product_details` (`id`),
    CONSTRAINT `fk_tbdpbi_tour` FOREIGN KEY (`tour_id`) REFERENCES `tour` (`id`),
    CONSTRAINT `fk_tbdpbi_date` FOREIGN KEY (`dates_id`) REFERENCES `dates` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
