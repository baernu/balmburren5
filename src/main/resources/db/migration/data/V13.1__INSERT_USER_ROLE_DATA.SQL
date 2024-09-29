INSERT INTO `user_and_role` (`optlock`, `role_id`, `user_id`) VALUES
                                                                                    (0, 2, 1),
                                                                                    (0, 1, 1) AS new;;
ON DUPLICATE KEY UPDATE `optlock`=new.`optlock`;