CREATE TABLE IF NOT EXISTS `product_info` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `category` varchar(255) DEFAULT NULL,
    `description` varchar(255) DEFAULT NULL,
    `price` double NOT NULL,
    `size` double NOT NULL,
    `unit` varchar(255) DEFAULT NULL,
    `optlock` int NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
    ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;