create DATABASE IF NOT EXISTS dr_crawler DEFAULT CHARACTER SET utf8mb4 DEFAULT COLLATE utf8mb4_general_ci;

USE dr_crawler;
-- ----------------------------
-- Table structure for t_company
-- ----------------------------
DROP TABLE IF EXISTS `t_company`;
CREATE TABLE `t_company` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `tyc_id` int(11) unsigned DEFAULT NULL COMMENT 'tyc公司id',
                            `company_name` VARCHAR(128) NULL DEFAULT NULL COMMENT '公司名称',
                            `stock_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '公司股票代码',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_tyc_id` (`tyc_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司信息';


-- ----------------------------
-- Table structure for t_company_stock
-- ----------------------------
DROP TABLE IF EXISTS `t_company_stock`;
CREATE TABLE `t_company_stock` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_id` int(11) unsigned DEFAULT NULL COMMENT '公司id',
                            `name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '公司名称',
                            `stock_code` VARCHAR(32) NULL NOT NULL COMMENT '公司股票代码',
                            `last_price` decimal(10,5) DEFAULT NULL COMMENT '公司股票最新价格',
                            `total_capitalization` decimal(30,5) DEFAULT NULL COMMENT '公司最新总市值',
                            `last_circulation` decimal(25,5) DEFAULT NULL COMMENT '公司最新流通市值',
                            `last_income` decimal(10,5) DEFAULT NULL COMMENT '公司最新收益',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_id` (`company_id`) USING BTREE,
                            KEY `index_last_circulation` (`last_circulation`) USING BTREE,
                            KEY `index_last_income` (`last_income`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司股票信息';

-- ----------------------------
-- Table structure for t_company_share_holder
-- ----------------------------
DROP TABLE IF EXISTS `t_company_share_holder`;
CREATE TABLE `t_company_share_holder` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT 't_company_stock主键',
                            `holder_type` char(2) DEFAULT NULL COMMENT '股东类型(1-股东，2-流通股东)',
                            `report_time` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '股东信息发布时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_stock_id` (`company_stock_id`) USING BTREE,
                            KEY `index_report_time` (`report_time`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股东信息记录';

-- ----------------------------
-- Table structure for t_company_share_holder
-- ----------------------------
DROP TABLE IF EXISTS `t_share_holder_detail`;
CREATE TABLE `t_share_holder_detail` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `holder_id` int(11) unsigned NOT NULL COMMENT 't_company_share_holder主键',
                            `holder_rank` int(11) unsigned DEFAULT NULL COMMENT '股东排名',
                            `holder_name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '持股人/机构名称',
                            `holder_type` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '股东性质',
                            `stock_type` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '股份类型',
                            `hold_count` bigint(64) unsigned DEFAULT NULL COMMENT '持股数(股)',
                            `hold_percent` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '占总流通/总股本持股比例',
                            `zj` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '增减',
                            `change_percent` varchar(36) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '变动比例',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            `updated_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `updated_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_holder_id` (`holder_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='股东信息详情';

-- ----------------------------
-- Table structure for t_total_statistics_record
-- ----------------------------
DROP TABLE IF EXISTS `t_total_stock_record`;
CREATE TABLE `t_total_stock_record` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `average_price` decimal(10,5) DEFAULT NULL COMMENT '所有公司股票平均价格',
                            `total_capitalization` decimal(40,5) DEFAULT NULL COMMENT '所有公司总市值',
                            `last_circulation` decimal(40,5) DEFAULT NULL COMMENT '所有公司流通市值',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `record_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司股票统计信息';

