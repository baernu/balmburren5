INSERT INTO `role`(`id`, `created_at`, `description`, `name`, `updated_at`, `optlock`) VALUES
                (1, '2024-09-28 09:19:07.800000', 'Default user role', 'USER', '2024-09-28 09:19:07.800000', 0),
                (2, '2024-09-28 09:19:07.813000', 'Administrator role', 'ADMIN', '2024-09-28 09:19:07.813000', 0),
                (3, '2024-09-28 09:19:07.817000', 'Super Administrator role', 'SUPER_ADMIN', '2024-09-28 09:19:07.817000', 0),
                (4, '2024-09-28 09:19:07.821000', 'Driver role', 'DRIVER', '2024-09-28 09:19:07.821000', 0),
                (5, '2024-09-28 09:19:07.826000', 'Helper role', 'KATHY', '2024-09-28 09:19:07.826000', 0),
                (6, '2024-09-28 09:19:07.830000', 'User administrated by Kathy', 'USER_KATHY', '2024-09-28 09:19:07.830000', 0);
ON DUPLICATE KEY UPDATE `created_at`=VALUES(`created_at`), `description`=VALUES(`description`), `name`=VALUES(`name`), `updated_at`=VALUES(`updated_at`), `optlock`=VALUES(`optlock`);
