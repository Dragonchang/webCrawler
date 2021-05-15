ALTER TABLE `t_focus`
ADD COLUMN `focus_price` decimal(10,5) DEFAULT NULL COMMENT '关注时候的价格' AFTER `stock_code`;