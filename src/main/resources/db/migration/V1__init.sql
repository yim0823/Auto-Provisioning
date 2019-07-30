-- 공통코드
CREATE TABLE `test02`.`user`
(
    `user_id`              bigint(20) NOT NULL AUTO_INCREMENT,
    `created_at`           datetime     DEFAULT NULL,
    `created_by`           varchar(255) DEFAULT NULL,
    `updated_at`           datetime     DEFAULT NULL,
    `update_by`            varchar(255) DEFAULT NULL,
    `city`                 varchar(30)  DEFAULT NULL,
    `company_bs`           varchar(30)  DEFAULT NULL,
    `company_catch_phrase` varchar(30)  DEFAULT NULL,
    `company_name`         varchar(30)  DEFAULT NULL,
    `email`                varchar(30)  DEFAULT NULL,
    `geo_lat`              varchar(30)  DEFAULT NULL,
    `geo_lng`              varchar(30)  DEFAULT NULL,
    `name`                 varchar(30)  DEFAULT NULL,
    `phone`                varchar(30)  DEFAULT NULL,
    `street`               varchar(30)  DEFAULT NULL,
    `suite`                varchar(30)  DEFAULT NULL,
    `user_name`            varchar(30)  DEFAULT NULL,
    `web_site`             varchar(30)  DEFAULT NULL,
    `zip_code`             varchar(30)  DEFAULT NULL,
    PRIMARY KEY (`user_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8;