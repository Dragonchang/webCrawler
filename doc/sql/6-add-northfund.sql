DROP TABLE IF EXISTS `t_north_fund`;
CREATE TABLE `t_north_fund` (
                            `id` int(11) unsigned NOT NULL AUTO_INCREMENT COMMENT '自增主键',
                            `plate_total_capitalization` decimal(30,5) DEFAULT NULL COMMENT '板块最新市值',
                            `report_time` varchar(32) CHARACTER SET utf8mb4 NOT NULL COMMENT '板块信息发布时间',
                            `deleted` char(1) CHARACTER SET utf8mb4 NOT NULL DEFAULT '0' COMMENT '是否删除(0-否，1-是)',
                            `created_by` varchar(32) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'sys',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='板块信息';