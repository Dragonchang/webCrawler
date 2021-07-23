DROP TABLE IF EXISTS `t_upward_trend`;
CREATE TABLE `t_upward_trend` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '上涨态势生成时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_stock_id` (`company_stock_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='上涨态势公司记录';

ALTER TABLE `t_upward_trend`
ADD COLUMN `avg_five` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "五日均价" AFTER `company_stock_id`,
ADD COLUMN `avg_ten` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "十日均价" AFTER `avg_five`,
ADD COLUMN `avg_twenty` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "20日均价" AFTER `avg_ten`,
ADD COLUMN `avg_thirty` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "30日均价" AFTER `avg_twenty`,
ADD COLUMN `avg_sixty` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "60日均价" AFTER `avg_thirty`,
ADD COLUMN `avg_ninety` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "90日均价" AFTER `avg_sixty`,
ADD COLUMN `avg_hundtwenty` VARCHAR(32) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT "120日均价" AFTER `avg_ninety`;

DROP TABLE IF EXISTS `t_new_upward_trend`;
CREATE TABLE `t_new_upward_trend` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `company_stock_id` int(11) unsigned DEFAULT NULL COMMENT '公司股票id',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '上涨态势生成时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE,
                            KEY `index_company_stock_id` (`company_stock_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='新增的上涨态势公司记录';