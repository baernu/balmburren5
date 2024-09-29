INSERT INTO `user`(`id`, `created_at`, `enabled`, `firstname`, `lastname`, `password`, `updated_at`, `username`, `optlock`) VALUES
    (1, '2024-09-28 09:19:07.918000', 1, 'Normal', 'Admin', '$2a$10$hX9k.0DH51hf7b2ggCpOiOrMkv1ly.gqXFILyfrQQipoTS12P3Bqq', '2024-09-28 09:19:07.918000', 'admin', 0) AS new;
ON DUPLICATE KEY UPDATE `created_at`= new.`created_at`, `enabled`=new.`enabled`, `firstname`=new.`firstname`, `lastname`=new.`lastname`, `password`=new.`password`, `updated_at`=new.`updated_at`, `username`=new.`username`, `optlock`=new.`optlock`;
