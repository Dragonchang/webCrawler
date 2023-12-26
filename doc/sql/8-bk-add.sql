DROP TABLE IF EXISTS `t_bk_info`;
CREATE TABLE `t_bk_info` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `bk_code`  varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块代号',
                            `bk_name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块名称',
                            `bk_last_price` decimal(30,5) DEFAULT NULL COMMENT '板块最新点数',
                            `bk_total_capitalization` decimal(50,5) DEFAULT NULL COMMENT '板块最新总市值',
                            `bk_last_up_down` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '今日装涨跌幅市值',
                            `bk_price_rate` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '今日装涨跌幅率',
                            `bk_exchange` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '换手率',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块信息';


DROP TABLE IF EXISTS `t_bk_stock`;
CREATE TABLE `t_bk_stock` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `bk_id` int(11) unsigned DEFAULT NULL COMMENT '板块id',
                            `bk_name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块名称',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `company_stock_name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '公司名称',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_bk_id` (`bk_id`) USING BTREE,
                            KEY `index_company_stock_id` (`company_stock_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司板块信息';