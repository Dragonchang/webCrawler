ALTER TABLE `t_company`
RENAME TO  `t_focus` ,
DROP COLUMN `tyc_id`,
ADD COLUMN `stock_company_id` INT(11) NULL DEFAULT NULL COMMENT "t_company_stock主键id" AFTER `id`,
ADD COLUMN `type` CHAR(1) NULL DEFAULT NULL COMMENT "关注类型 1.股份公司 2.机构 3.个人" AFTER `stock_code`;