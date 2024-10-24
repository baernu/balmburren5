CREATE TABLE IF NOT EXISTS `invoice` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `amount` double NOT NULL,
    `is_paid` bit(1) DEFAULT NULL,
    `paid` double NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    `is_sent` bit(1) DEFAULT NULL,
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;