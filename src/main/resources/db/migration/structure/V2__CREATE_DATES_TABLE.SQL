CREATE TABLE IF NOT EXISTS `dates` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `date` varchar(255) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;