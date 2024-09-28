CREATE TABLE IF NOT EXISTS `invoice` (
    `id` bigint NOT NULL,
    `amount` double NOT NULL,
    `is_paid` bit(1) DEFAULT NULL,
    `paid` double NOT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;