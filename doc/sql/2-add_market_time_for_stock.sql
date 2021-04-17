ALTER TABLE `t_company_stock`
ADD COLUMN `market_time` timestamp DEFAULT NULL COMMENT '公司上市时间' AFTER `last_income`;