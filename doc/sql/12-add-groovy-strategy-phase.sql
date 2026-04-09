-- 第二阶段：Groovy策略执行器数据库升级脚本
-- 说明：在第一阶段策略管理基础上，支持 Groovy 脚本执行、脚本校验与执行器标识

ALTER TABLE `t_strategy_version`
ADD COLUMN `script_type` varchar(32) DEFAULT 'RULE' COMMENT '脚本类型 RULE/GROOVY' AFTER `version_no`;

ALTER TABLE `t_strategy_run`
ADD COLUMN `engine_type` varchar(32) DEFAULT 'RULE' COMMENT '执行器类型 RULE/GROOVY' AFTER `strategy_version_id`,
ADD COLUMN `script_type` varchar(32) DEFAULT 'RULE' COMMENT '脚本类型快照 RULE/GROOVY' AFTER `engine_type`;

ALTER TABLE `t_strategy_info`
ADD COLUMN `validate_status` varchar(16) DEFAULT NULL COMMENT '最近校验状态 SUCCESS/FAIL' AFTER `last_run_status`,
ADD COLUMN `validate_message` varchar(1000) DEFAULT NULL COMMENT '最近校验信息' AFTER `validate_status`;

UPDATE `t_strategy_version` v
LEFT JOIN `t_strategy_info` i ON v.strategy_id = i.id
SET v.script_type = IFNULL(i.script_type, 'RULE')
WHERE v.script_type IS NULL OR v.script_type = '';

