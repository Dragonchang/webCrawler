DROP TABLE IF EXISTS `t_plate`;
CREATE TABLE `t_plate` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块名称',
                            `plate_price` decimal(10,5) DEFAULT NULL COMMENT '板块最新价格',
                            `plate_total_capitalization` decimal(30,5) DEFAULT NULL COMMENT '板块最新市值',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块信息';

DROP TABLE IF EXISTS `t_plate_price_record`;
CREATE TABLE `t_plate_price_record` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `plate_id` int(11) unsigned DEFAULT NULL COMMENT '板块id',
                            `plate_price` decimal(10,5) DEFAULT NULL COMMENT '板块最新价格',
                            `plate_total_capitalization` decimal(30,5) DEFAULT NULL COMMENT '板块最新市值',
                            `company_count` int(11) unsigned DEFAULT NULL COMMENT '板块公司计数',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块信息发布时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块变动记录信息';

DROP TABLE IF EXISTS `t_plate_company`;
CREATE TABLE `t_plate_company` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `plate_id` int(11) unsigned DEFAULT NULL COMMENT '板块id',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块公司信息';

