CREATE TABLE IF NOT EXISTS `user_and_tour` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `position` int DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `end_date_id` bigint DEFAULT NULL,
    `start_date_id` bigint DEFAULT NULL,
    `tour_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
     PRIMARY KEY (`id`),
    KEY `idx_end_date_id` (`end_date_id`),
    KEY `idx_start_date_id` (`start_date_id`),
    KEY `idx_tour_id` (`tour_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_ut_end_date` FOREIGN KEY (`end_date_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_ut_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_ut_start_date` FOREIGN KEY (`start_date_id`) REFERENCES `dates` (`id`),
    CONSTRAINT `fk_ut_tour` FOREIGN KEY (`tour_id`) REFERENCES `tour` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;