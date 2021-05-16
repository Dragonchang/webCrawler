ALTER TABLE `t_focus`
ADD COLUMN `focus_price` decimal(10,5) DEFAULT NULL COMMENT '关注时候的价格' AFTER `stock_code`;

DROP TABLE IF EXISTS `t_north_capital`;
CREATE TABLE `t_north_capital` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `stock_company_id` INT(11) NOT NULL COMMENT 't_company_stock主键id',
                            `name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '公司名称',
                            `stock_code` VARCHAR(32) NULL NOT NULL COMMENT '公司股票代码',
                            `hold_count` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '持股数(股)',
                            `market_value` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '持股市值',
                            `increase_hold_count` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '当日增持股数(股)',
                            `increase_market_value` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '当日增持股市值',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='北向资金单个公司增减持信息';