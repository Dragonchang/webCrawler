ALTER TABLE `t_focus`
ADD COLUMN `focus_price` decimal(10,5) DEFAULT NULL COMMENT '关注时候的价格' AFTER `stock_code`;

-- ----------------------------
-- Table structure for t_company_stock_record
-- ----------------------------
DROP TABLE IF EXISTS `t_company_price_record`;
CREATE TABLE `t_company_price_record` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `open_price` VARCHAR(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '开盘价格',
                            `close_price` VARCHAR(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '收盘价格',
                            `highest_price` VARCHAR(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '最高价格',
                            `lowest_price` VARCHAR(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '最低价格',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '股价信息发布时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司股票信息';

DROP TABLE IF EXISTS `t_finance_analysis`;
CREATE TABLE `t_finance_analysis` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `stock_company_id` INT(11) NOT NULL COMMENT 't_company_stock主键id',
                            `total_income` decimal(40,5) DEFAULT NULL COMMENT '季度总营收',
                            `net_profit` decimal(40,5) DEFAULT NULL COMMENT '扣非利润',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '财务信息发布时间',
                            `report_type` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '报告类型1一季度报，2中报，3三季度报，4 年报',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司财务信息表';


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