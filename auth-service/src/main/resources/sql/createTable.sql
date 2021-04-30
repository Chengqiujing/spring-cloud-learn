DROP TABLE IF EXISTS `clientdetails`;
CREATE TABLE `clientdetails`
(
    `appId`                  varchar(128) NOT NULL,
    `resourceIds`            varchar(255)  DEFAULT NULL,
    `appSecret`              varchar(255)  DEFAULT NULL,
    `scope`                  varchar(255)  DEFAULT NULL,
    `grantTypes`             varchar(255)  DEFAULT NULL,
    `redirectUrl`            varchar(255)  DEFAULT NULL,
    `authorities`            varchar(255)  DEFAULT NULL,
    `access_token_validity`  int(11) DEFAULT NULL,
    `refresh_token_validity` int(11) DEFAULT NULL,
    `additionalInformation`  varchar(4096) DEFAULT NULL,
    `autoApproveScopes`      varchar(255)  DEFAULT NULL,
    PRIMARY KEY (`appId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_access_token`;
CREATE TABLE `oauth_access_token`
(
    `token_id`          varchar(256) NOT NULL,
    `token`             blob,
    `authentication_id` varchar(128) NOT NULL,
    `user_name`         varchar(255) DEFAULT NULL,
    `client_id`         varchar(256) DEFAULT NULL,
    `authentication`    blob,
    `refresh_token`     varchar(255) DEFAULT NULL,
    PRIMARY KEY (`authentication_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_approvals`;
CREATE TABLE `oauth_approvals`
(
    `userId`         varchar(256) NOT NULL,
    `clientId`       varchar(256) DEFAULT NULL,
    `scope`          varchar(255) DEFAULT NULL,
    `status`         varchar(10)  DEFAULT NULL,
    `expirestAt`     datetime     DEFAULT NULL,
    `lastModifiedAt` datetime     DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_details`;
CREATE TABLE `oauth_client_details`
(
    `client_id`               varchar(256) NOT NULL,
    `resource_ids`            varchar(255)  DEFAULT NULL,
    `client_secret`           varchar(255)  DEFAULT NULL,
    `scope`                   varchar(255)  DEFAULT NULL,
    `authorized_grant_types`  varchar(255)  DEFAULT NULL,
    `web_server_redirect_uri` varchar(255)  DEFAULT NULL,
    `authorities`             varchar(255)  DEFAULT NULL,
    `access_token_validity`   int(11) DEFAULT NULL,
    `refresh_token_validity`  int(11) DEFAULT NULL,
    `additional_information`  varchar(4096) DEFAULT NULL,
    `autoapprove`             varchar(255)  DEFAULT NULL,
    PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_token`;
CREATE TABLE `oauth_client_token`
(
    `token_id`           varchar(256) NOT NULL,
    `token`              blob,
    `authenticatioin_id` varchar(128) NOT NULL,
    `user_name`          varchar(255) DEFAULT NULL,
    `client_id`          varchar(256) DEFAULT NULL,
    PRIMARY KEY (`authenticatioin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_code`;
CREATE TABLE `oauth_code`
(
    `code`           varchar(255) DEFAULT NULL,
    `authentication` blob
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_refresh_token`;
CREATE TABLE `oauth_refresh_token`
(
    `token_id`       varchar(256) NOT NULL,
    `token`          blob,
    `authentication` blob,
    PRIMARY KEY (`token_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id` bigint(
) NOT NULL AUTO_INCREMENT,
    `name` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`       bitint(20) NOT NULL AUTO_INCREMENT,
    `password` varchar(255) COLLATE utf8_bin DEFAULT NULL,
    `username` varchar(255) COLLATE utf8_bin NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `UK_sb8bbouer5wak8vyiiy4pf2bx` (`username`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `user_id` bigint(20) NOT NULL,
    `role_id` bigint(20) NOT NULL,
    KEY       `FKAkdfjlandgu7a987932r` (`role_id`),
    KEY       `FK987asdf9d8gu0ad0f8f` (`user_id`),
    CONSTRAINT `83762tr73t4ty9` FOREIGN KEY (`role_id`) REFERENCES `role` (`id`),
    CONSTRAINT `8u4t9393hy34u` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;