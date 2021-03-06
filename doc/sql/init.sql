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
-- Table structure for t_share_structure
-- ----------------------------
DROP TABLE IF EXISTS `t_share_structure`;
CREATE TABLE `t_share_structure` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_id` int(11) unsigned DEFAULT NULL COMMENT '公司id',
                            `share_company_name` varchar(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '参股公司名称',
                            `share_company_stock_code` VARCHAR(32) NULL DEFAULT NULL COMMENT '参股公司股票代码',
                            `share_company_amount` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '参股金额',
                            `share_company_type` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '公司类型',
                            `share_company_bond_type` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '参股公司债券类型 ',
                            `share_company_finance_label` varchar(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '股权类型',
                            `share_company_percent` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '参股比例',
                            `share_company_brand` varchar(512) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '参股公司品牌',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_id` (`company_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司参股信息';

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
                            KEY `index_company_id` (`company_id`) USING BTREE
                            KEY `index_last_circulation` (`last_circulation`) USING BTREE
                            KEY `index_last_income` (`last_income`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司股票信息';



-- ----------------------------
-- Table structure for t_company_stock_record
-- ----------------------------
DROP TABLE IF EXISTS `t_company_stock_record`;
CREATE TABLE `t_company_stock_record` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `volume ` VARCHAR(512) CHARACTER SET utf8mb4 NOT NULL COMMENT '成交量',
                            `turnover ` VARCHAR(512) NULL NOT NULL COMMENT '成交额',
                            `registered_capital ` VARCHAR(512) NULL NOT NULL COMMENT '注册股本',
                            `share_capital ` VARCHAR(512) NULL NOT NULL COMMENT '发行股本',
                            `circulation_capital` VARCHAR(512) NULL NOT NULL COMMENT '流通股本',
                            `last_price` VARCHAR(512) NULL NOT NULL COMMENT '公司股票最新价格',
                            `last_price` VARCHAR(512) NULL NOT NULL COMMENT '公司股票最新价格',
                            `last_price` VARCHAR(512) NULL NOT NULL COMMENT '公司股票最新价格',
                            `last_price` VARCHAR(512) NULL NOT NULL COMMENT '公司股票最新价格',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `record_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '记录时间',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_id` (`company_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='公司股票信息';



