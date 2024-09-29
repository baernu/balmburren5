CREATE TABLE IF NOT EXISTS `work` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `description` varchar(255) DEFAULT NULL,
    `end_time` time(6) DEFAULT NULL,
    `start_time` time(6) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `work_time` varchar(255) DEFAULT NULL,
    `date_id` bigint DEFAULT NULL,
    `user_id` bigint DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_date_id` (`date_id`),
    KEY `idx_user_id` (`user_id`),
    CONSTRAINT `fk_w_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
    CONSTRAINT `fk_w_date` FOREIGN KEY (`date_id`) REFERENCES `dates` (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
