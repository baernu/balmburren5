CREATE TABLE IF NOT EXISTS `address` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `a_latitude` double NOT NULL,
    `a_longitude` double NOT NULL,
    `city` varchar(255) DEFAULT NULL,
    `number` varchar(255) DEFAULT NULL,
    `plz` int NOT NULL,
    `street` varchar(255) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;