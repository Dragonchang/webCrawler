-- 策略管理系统第一阶段建表脚本
-- 说明：适用于 webCrawler 项目第一阶段策略管理模块

CREATE TABLE IF NOT EXISTS `t_strategy_info` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `strategy_code` varchar(64) NOT NULL COMMENT '策略编码',
  `strategy_name` varchar(128) NOT NULL COMMENT '策略名称',
  `category` varchar(32) DEFAULT NULL COMMENT '策略分类',
  `description` varchar(1000) DEFAULT NULL COMMENT '策略描述',
  `script_type` varchar(32) DEFAULT 'RULE' COMMENT '脚本类型',
  `script_content` longtext COMMENT '当前编辑态脚本内容',
  `param_schema` text COMMENT '参数定义JSON',
  `default_params` text COMMENT '默认参数JSON',
  `universe_config` text COMMENT '股票池配置JSON',
  `latest_version_no` int(11) DEFAULT 0 COMMENT '最新版本号',
  `published_version_no` int(11) DEFAULT NULL COMMENT '发布版本号',
  `status` tinyint(4) DEFAULT 0 COMMENT '状态 0草稿 1启用 2停用',
  `schedule_type` varchar(16) DEFAULT 'MANUAL' COMMENT '调度类型',
  `cron_expr` varchar(64) DEFAULT NULL COMMENT 'cron表达式',
  `last_run_time` datetime DEFAULT NULL COMMENT '最近运行时间',
  `last_run_status` varchar(16) DEFAULT NULL COMMENT '最近运行状态',
  `deleted` tinyint(4) DEFAULT 0 COMMENT '是否删除，0未删除，1已删除',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  `updated_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  `updated_by` varchar(64) DEFAULT NULL COMMENT '更新人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_strategy_code` (`strategy_code`),
  KEY `idx_strategy_status` (`status`),
  KEY `idx_strategy_updated_time` (`updated_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略主表';

CREATE TABLE IF NOT EXISTS `t_strategy_version` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `version_no` int(11) NOT NULL COMMENT '版本号',
  `script_content` longtext COMMENT '脚本内容',
  `param_schema` text COMMENT '参数定义JSON',
  `default_params` text COMMENT '默认参数JSON',
  `universe_config` text COMMENT '股票池配置JSON',
  `script_hash` varchar(64) DEFAULT NULL COMMENT '脚本摘要',
  `is_published` tinyint(4) DEFAULT 1 COMMENT '是否已发布',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `created_by` varchar(64) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uk_strategy_version` (`strategy_id`,`version_no`),
  KEY `idx_strategy_version_strategy_id` (`strategy_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略版本表';

CREATE TABLE IF NOT EXISTS `t_strategy_run` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运行实例ID',
  `strategy_id` bigint(20) NOT NULL COMMENT '策略ID',
  `strategy_version_id` bigint(20) NOT NULL COMMENT '策略版本ID',
  `run_type` varchar(16) DEFAULT 'MANUAL' COMMENT '运行方式 MANUAL/SCHEDULE/RETRY',
  `trigger_source` varchar(32) DEFAULT 'USER' COMMENT '触发来源 USER/SYSTEM/API',
  `run_status` varchar(16) DEFAULT 'PENDING' COMMENT '运行状态',
  `param_snapshot` text COMMENT '参数快照JSON',
  `data_snapshot_date` varchar(32) DEFAULT NULL COMMENT '数据快照日期',
  `universe_snapshot` text COMMENT '股票池快照JSON',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `duration_ms` bigint(20) DEFAULT NULL COMMENT '耗时毫秒',
  `result_count` int(11) DEFAULT 0 COMMENT '结果数量',
  `error_message` varchar(2000) DEFAULT NULL COMMENT '错误摘要',
  `created_by` varchar(64) DEFAULT NULL COMMENT '触发人',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_run_strategy_id` (`strategy_id`),
  KEY `idx_strategy_run_status` (`run_status`),
  KEY `idx_strategy_run_created_time` (`created_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略运行实例表';

CREATE TABLE IF NOT EXISTS `t_strategy_run_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `run_id` bigint(20) NOT NULL COMMENT '运行实例ID',
  `line_no` int(11) DEFAULT NULL COMMENT '日志序号',
  `log_level` varchar(16) DEFAULT 'INFO' COMMENT '日志级别',
  `log_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '日志时间',
  `content` text COMMENT '日志内容',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_run_log_run_id` (`run_id`,`line_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略运行日志表';

CREATE TABLE IF NOT EXISTS `t_strategy_result` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `run_id` bigint(20) NOT NULL COMMENT '运行实例ID',
  `stock_id` int(11) DEFAULT NULL COMMENT '股票ID',
  `stock_code` varchar(32) DEFAULT NULL COMMENT '股票代码',
  `stock_name` varchar(128) DEFAULT NULL COMMENT '股票名称',
  `action_type` varchar(16) DEFAULT 'WATCH' COMMENT '动作类型',
  `score` decimal(10,4) DEFAULT 0 COMMENT '综合评分',
  `reason` varchar(2000) DEFAULT NULL COMMENT '入选原因',
  `factor_detail` text COMMENT '因子明细JSON',
  `rank_no` int(11) DEFAULT NULL COMMENT '排名',
  `created_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `idx_strategy_result_run_id` (`run_id`,`rank_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='策略运行结果表';

