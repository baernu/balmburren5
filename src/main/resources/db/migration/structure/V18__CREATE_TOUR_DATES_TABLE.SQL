CREATE TABLE IF NOT EXISTS `tour_and_dates` (
    `id` bigint NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `dates_id` bigint DEFAULT NULL,
    `tour_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_dates_id` (`dates_id`),
    KEY `idx_tour_id` (`tour_id`),
    CONSTRAINT `fk_td_tour` FOREIGN KEY (`tour_id`) REFERENCES `tour` (`id`),
    CONSTRAINT `fk_td_date` FOREIGN KEY (`dates_id`) REFERENCES `dates` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;