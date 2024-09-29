INSERT INTO `user_and_role` (`optlock`, `role_id`, `user_id`) VALUES
                                                                                    (0, 2, 1),
                                                                                    (0, 1, 1);
ON DUPLICATE KEY UPDATE `optlock`=VALUES(`optlock`);