DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(255) NOT NULL,
  `username` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `user` VALUES ('aasfsfsnolfjs', 'admin', '123456');

DROP TABLE IF EXISTS `post`;
CREATE TABLE `post` (
  `id` varchar(255) NOT NULL,
  `name` varchar(255) NOT NULL,
  `content` text,
  `click` int(11) DEFAULT NULL,
  `created_at` timestamp DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
INSERT INTO `post` VALUES ('386158923117367296', 'haha', 'good', '0', '2019-10-28 11:38:47', '2019-10-28 11:38:47');
