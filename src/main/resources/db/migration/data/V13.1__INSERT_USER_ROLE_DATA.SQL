-- INSERT INTO `user_and_role` (`optlock`, `role_id`, `user_id`) VALUES
--                                                                                     (0, 2, 1),
--                                                                                     (0, 1, 1) AS new;;
-- ON DUPLICATE KEY UPDATE `optlock`=new.`optlock`;

INSERT INTO `user_and_role` (`id`,`optlock`, `role_id`, `user_id`)
VALUES
    (8, 0, 3, 1),
    (9, 0, 2, 1)
    ON DUPLICATE KEY UPDATE
                         `optlock` = VALUES(`optlock`);

ALTER TABLE `user_and_role` AUTO_INCREMENT = 10;