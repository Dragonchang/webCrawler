# Groovy策略执行器详细设计说明书

## 1. 文档信息

| 项目 | 内容 |
|---|---|
| 文档名称 | Groovy策略执行器详细设计说明书 |
| 所属项目 | `webCrawler` |
| 文档版本 | V1.0 |
| 所属阶段 | 第二阶段 |
| 编写日期 | 2026-04-09 |
| 编写说明 | 基于第一阶段策略管理系统实现，设计第二阶段 Groovy 脚本执行能力 |

---

## 2. 编制目的

本文档用于说明 `webCrawler` 项目第二阶段 Groovy 策略执行器的设计方案，指导后续数据库升级、后端执行器开发、前端页面改造和 Groovy 脚本模板提供工作。

本文档主要解决以下问题：

1. 第一阶段固定规则执行方式如何升级为 Groovy 脚本执行；
2. 如何保证 Groovy 策略执行的安全性、可控性和可追溯性；
3. 如何与当前已有的策略主表、版本表、运行表、日志表、结果表进行集成；
4. 如何为第三阶段回测和更复杂策略执行打基础。

---

## 3. 当前现状说明

当前第一阶段已完成以下能力：

1. 策略列表、策略编辑、运行记录、运行详情页面；
2. 策略主表 `t_strategy_info`；
3. 策略版本表 `t_strategy_version`；
4. 策略运行实例表 `t_strategy_run`；
5. 策略日志表 `t_strategy_run_log`；
6. 策略结果表 `t_strategy_result`；
7. 基于固定 Java 方法的规则执行逻辑。

当前执行逻辑主要问题如下：

1. `script_content` 仅作为脚本文本保存，并未真正执行；
2. 执行逻辑写死在 `StrategyRunService` 中；
3. 用户无法通过脚本定义复杂策略；
4. 每次新增规则都需要修改 Java 代码；
5. 缺少脚本校验、脚本缓存和脚本执行安全控制。

因此第二阶段需引入 Groovy 执行器，将“脚本文本”升级为“可受控执行的策略程序”。

---

## 4. 第二阶段建设目标

第二阶段建设目标如下：

1. 支持 `scriptType = GROOVY` 的策略；
2. 支持 Groovy 策略脚本在线编辑；
3. 支持 Groovy 脚本校验；
4. 支持脚本编译缓存；
5. 支持脚本执行；
6. 支持脚本访问平台受限 API；
7. 支持脚本输出日志和结果；
8. 支持脚本运行超时控制；
9. 支持与第一阶段规则执行方式并存。

---

## 5. 设计原则

### 5.1 与第一阶段兼容

第二阶段必须兼容第一阶段已有策略数据和运行方式。系统需支持：

- `RULE`：固定规则执行器
- `GROOVY`：Groovy 脚本执行器

### 5.2 最小开放能力

Groovy 脚本不应直接访问系统全部能力，只允许调用平台显式开放的 API。

### 5.3 可追踪

每次 Groovy 执行都应保留：

- 编译状态
- 执行日志
- 运行结果
- 执行器类型
- 异常摘要

### 5.4 安全可控

必须限制：

- 文件访问
- 网络访问
- 线程创建
- 反射逃逸
- 长时间运行

### 5.5 便于扩展

Groovy 执行器设计要为未来回测执行器、信号执行器和调度执行器留出空间。

---

## 6. 总体架构设计

第二阶段建议将当前策略执行逻辑拆分为“运行编排层 + 执行器层 + 脚本层 + API层 + 数据支撑层”。

### 6.1 分层结构

```text
策略运行编排层
├── StrategyRunService
├── StrategyExecutorFactory
└── StrategyExecuteContext

执行器层
├── RuleStrategyExecutor
└── GroovyStrategyExecutor

脚本层
├── GroovyScriptCompiler
├── GroovyScriptCache
├── GroovyBindingFactory
└── GroovyScriptValidator

API层
├── StrategyApi
├── ResultApi
└── LogApi

数据支撑层
├── StrategyUniverseService
├── StrategyFinanceDataService
└── StrategyMarketDataService
```

---

## 7. 核心流程设计

### 7.1 执行流程

```text
用户点击运行策略
→ StrategyRunService 创建运行实例
→ 查询策略信息与发布版本
→ 构建 StrategyExecuteContext
→ StrategyExecutorFactory 根据 scriptType 选择执行器
→ 若为 RULE，则调用 RuleStrategyExecutor
→ 若为 GROOVY，则调用 GroovyStrategyExecutor
→ 执行器输出 StrategyExecuteResult
→ StrategyRunService 统一保存日志、结果、状态
→ 页面展示运行详情
```

### 7.2 Groovy 执行流程

```text
读取 StrategyVersion.scriptContent
→ 校验脚本是否合法
→ 计算 scriptHash
→ 查询缓存是否已编译
→ 若未编译则执行编译
→ 创建 Binding
→ 注入 api、params、context、result、log
→ 执行脚本
→ 收集结果与日志
→ 返回统一执行结果
```

---

## 8. 数据库设计调整

## 8.1 调整目标

为了支持第二阶段 Groovy 执行器，需要补充以下能力：

1. 记录版本级脚本类型；
2. 记录运行实例级执行器类型；
3. 记录最近一次脚本校验状态；
4. 支持运行详情显示本次执行器类型。

## 8.2 SQL 脚本

数据库升级脚本已输出到：

`doc/sql/12-add-groovy-strategy-phase.sql`

## 8.3 表结构变更说明

### `t_strategy_version`
新增：

- `script_type`：记录该版本的脚本类型 `RULE/GROOVY`

### `t_strategy_run`
新增：

- `engine_type`：记录本次执行器类型 `RULE/GROOVY`
- `script_type`：记录本次执行时脚本类型快照

### `t_strategy_info`
新增：

- `validate_status`：最近一次脚本校验状态 `SUCCESS/FAIL`
- `validate_message`：最近一次脚本校验信息

---

## 9. 核心对象设计

## 9.1 StrategyExecuteContext

用于封装执行上下文，建议包含：

1. `runId`
2. `strategyInfo`
3. `strategyVersion`
4. `params`
5. `dataSnapshotDate`
6. `universe`

### 示例字段

```java
private Long runId;
private StrategyInfo strategyInfo;
private StrategyVersion strategyVersion;
private JSONObject params;
private String dataSnapshotDate;
private List<CompanyStock> universe;
```

## 9.2 StrategyExecuteResult

用于统一执行器输出，建议包含：

1. `results`
2. `logs`
3. `resultCount`
4. `summary`

### 示例字段

```java
private List<StrategyResult> results;
private List<StrategyRunLog> logs;
private Integer resultCount;
private String summary;
```

---

## 10. 执行器设计

## 10.1 StrategyExecutor 接口

建议定义统一接口：

```java
public interface StrategyExecutor {
    StrategyExecuteResult execute(StrategyExecuteContext context);
}
```

## 10.2 RuleStrategyExecutor

职责：

1. 保留第一阶段固定参数筛选执行逻辑；
2. 将 `StrategyRunService` 中当前写死的 `executeRule` 逻辑迁移至该执行器；
3. 继续支持历史规则型策略。

## 10.3 GroovyStrategyExecutor

职责：

1. 获取 Groovy 脚本；
2. 校验 Groovy 脚本；
3. 编译 Groovy 脚本；
4. 加载缓存；
5. 注入 Binding；
6. 执行脚本；
7. 收集日志与结果；
8. 返回统一结果对象。

---

## 11. Groovy 脚本层设计

## 11.1 GroovyScriptCompiler

职责：

1. 负责 Groovy 脚本编译；
2. 负责配置安全编译环境；
3. 返回编译后的 `Class<? extends Script>`。

## 11.2 GroovyScriptCache

职责：

1. 按 `scriptHash` 缓存已编译脚本；
2. 避免重复编译提升性能；
3. 支持策略版本缓存复用。

缓存 key 建议：

- `scriptHash`

缓存 value 建议：

- `Class<? extends Script>`

## 11.3 GroovyBindingFactory

职责：

1. 为每次运行构造独立 Binding；
2. 注入 `api`、`params`、`context`、`result`、`log`；
3. 避免脚本共享状态。

## 11.4 GroovyScriptValidator

职责：

1. 校验脚本能否编译；
2. 校验是否引用危险类；
3. 提前返回语法错误；
4. 供前端“校验脚本”按钮调用。

---

## 12. Binding 设计

每次 Groovy 执行前创建新的 Binding，注入以下变量：

### 12.1 `api`
统一策略 API 入口。

### 12.2 `params`
本次运行参数 JSON 对象。

### 12.3 `context`
当前运行上下文对象。

### 12.4 `result`
结果输出器。

### 12.5 `log`
日志输出器。

### 12.6 示例

```java
Binding binding = new Binding();
binding.setVariable("api", strategyApi);
binding.setVariable("params", params);
binding.setVariable("context", executeContext);
binding.setVariable("result", resultApi);
binding.setVariable("log", logApi);
```

---

## 13. API 层设计

Groovy 脚本不应直接访问数据库 Mapper，而应通过平台受控 API 访问数据。

## 13.1 StrategyApi

建议提供以下方法：

1. `stocks()`：获取股票池
2. `stocksByBk(String bkName)`：按板块获取股票
3. `stock(Integer stockId)`：获取单只股票
4. `finance(Integer stockId)`：获取最近一期财务信息
5. `prices(Integer stockId, Integer days)`：获取最近 N 日价格
6. `score(CompanyStock stock)`：计算平台统一评分
7. `toBigDecimal(Object value)`：类型辅助转换

## 13.2 ResultApi

用于脚本输出结果。

建议方法：

1. `add(Map<String, Object> item)`
2. `getResults()`

职责：

- 校验脚本输出字段；
- 自动封装为 `StrategyResult`；
- 自动设置 `runId` 与创建时间。

## 13.3 LogApi

用于脚本输出日志。

建议方法：

1. `info(String message)`
2. `warn(String message)`
3. `error(String message)`
4. `getLogs()`

职责：

- 自动追加 `runId`；
- 自动维护 `lineNo`；
- 自动记录日志时间。

---

## 14. 数据支撑层设计

## 14.1 StrategyUniverseService

职责：

1. 根据 `universeConfig` 加载股票池；
2. 支持全市场、板块、指定股票列表；
3. 复用当前第一阶段 `loadUniverse` 逻辑并独立封装。

## 14.2 StrategyFinanceDataService

职责：

1. 获取最近一期财务数据；
2. 为脚本提供统一财务访问接口；
3. 后续便于加入缓存。

## 14.3 StrategyMarketDataService

职责：

1. 获取最近 N 日价格数据；
2. 获取最新价格；
3. 为 Groovy 脚本计算均线、涨幅等能力提供支撑。

---

## 15. 安全设计

Groovy 执行器的重点不是“能运行”，而是“安全可控地运行”。

## 15.1 禁止能力

脚本不得：

1. 访问文件系统；
2. 发起网络请求；
3. 创建线程；
4. 反射访问系统内部对象；
5. 调用系统命令；
6. 自行构造 GroovyShell 或 ClassLoader。

## 15.2 受限类与包

建议禁止以下包或类：

- `java.io.*`
- `java.nio.*`
- `java.net.*`
- `java.lang.System`
- `java.lang.Runtime`
- `java.lang.ProcessBuilder`
- `java.lang.ClassLoader`
- `java.lang.reflect.*`
- `groovy.lang.GroovyShell`

## 15.3 SecureASTCustomizer

建议通过 `SecureASTCustomizer` 做第一层限制：

1. 禁止危险 import；
2. 限制某些表达式；
3. 限制危险语法结构；
4. 控制脚本编译阶段的语法边界。

## 15.4 最小 Binding 原则

不将以下对象注入 Groovy：

- Spring `ApplicationContext`
- 各类 Mapper
- 任意 Service Bean
- `ClassLoader`

---

## 16. 超时与并发控制设计

## 16.1 超时控制

Groovy 脚本执行必须放入可控线程池，使用 `Future.get(timeout)` 控制最大运行时长。

建议超时：

- 普通策略：10~30 秒

超时处理：

1. 中断任务；
2. 运行状态置为 `TIMEOUT`；
3. 记录超时日志；
4. 页面展示超时原因。

## 16.2 并发控制

建议第一阶段 Groovy 运行控制规则：

1. 单策略同一时刻只允许一个运行实例；
2. 全局执行线程池大小配置化；
3. 手动运行和调度运行共享统一执行池。

---

## 17. 前端页面改造设计

## 17.1 策略编辑页改造

需新增以下内容：

1. 脚本类型支持 `RULE` 和 `GROOVY`；
2. Groovy 脚本帮助文案；
3. “校验脚本”按钮；
4. “填充Groovy示例”按钮；
5. 编辑器模式改为 Groovy/Java 风格高亮。

## 17.2 页面交互逻辑

### 当脚本类型为 `RULE`
- 展示第一阶段说明；
- 保留默认参数 JSON；
- 运行时走规则执行器。

### 当脚本类型为 `GROOVY`
- 展示 Groovy 示例帮助；
- 支持脚本校验；
- 运行时走 Groovy 执行器。

## 17.3 运行详情页改造

建议新增展示：

1. `engineType`
2. `scriptType`
3. 脚本校验状态
4. 脚本异常位置（如有）

---

## 18. 后端接口设计

## 18.1 新增脚本校验接口

建议新增：

- `POST /strategy/validate`

请求参数：

```json
{
  "id": 1,
  "scriptType": "GROOVY",
  "scriptContent": "..."
}
```

返回示例：

```json
{
  "code": "0",
  "message": "校验成功"
}
```

失败示例：

```json
{
  "code": "1",
  "message": "第3行语法错误：unexpected token"
}
```

## 18.2 运行接口兼容设计

现有运行接口 `POST /strategyRun/execute` 保持不变。

运行时由后台根据 `strategyInfo.scriptType` 自动选择执行器。

---

## 19. 后端改造清单

## 19.1 现有类改造

### `StrategyRunService`
改造为：

1. 负责创建运行实例；
2. 负责构建执行上下文；
3. 负责通过执行器工厂选择执行器；
4. 负责统一落库日志与结果；
5. 不再直接持有具体 `executeRule` 主逻辑。

### `StrategyService`
新增能力：

1. 发布时保存 `scriptType` 到版本表；
2. 脚本校验状态回写到策略主表；
3. 提供校验接口支持。

## 19.2 新增类

建议新增：

1. `StrategyExecutor`
2. `RuleStrategyExecutor`
3. `GroovyStrategyExecutor`
4. `StrategyExecuteContext`
5. `StrategyExecuteResult`
6. `StrategyExecutorFactory`
7. `GroovyScriptCompiler`
8. `GroovyScriptCache`
9. `GroovyBindingFactory`
10. `GroovyScriptValidator`
11. `StrategyApi`
12. `ResultApi`
13. `LogApi`
14. `StrategyUniverseService`
15. `StrategyFinanceDataService`
16. `StrategyMarketDataService`

---

## 20. 示例 Groovy 脚本设计

以下脚本可作为第二阶段上线后的可运行示例脚本。

```groovy
log.info("开始执行 Groovy 策略")

def minMarketCap = params.minMarketCap ?: 100

def maxPe = params.maxPe ?: 30

def minPrice = params.minPrice ?: 5

def minIncome = params.minIncome ?: 1000000000

def limitCount = params.limit ?: 20

def candidates = api.stocks()
    .findAll { it.lastPrice != null && it.lastPrice >= minPrice }
    .findAll { it.totalCapitalization != null && it.totalCapitalization >= minMarketCap }
    .findAll { stock ->
        if (stock.syl == null || stock.syl.toString().trim().isEmpty()) {
            return false
        }
        try {
            return new BigDecimal(stock.syl.toString()) <= maxPe
        } catch (Exception ex) {
            return false
        }
    }
    .findAll { stock ->
        def finance = api.finance(stock.id)
        finance != null && finance.totalIncome != null && finance.totalIncome >= minIncome
    }
    .sort { a, b -> b.totalCapitalization <=> a.totalCapitalization }
    .take(limitCount)

int rank = 1
candidates.each { stock ->
    def finance = api.finance(stock.id)
    result.add([
        stockId: stock.id,
        stockCode: stock.stockCode,
        stockName: stock.name,
        actionType: "WATCH",
        score: api.score(stock),
        reason: "满足Groovy策略中的市值、价格、市盈率和营收条件",
        factorDetail: [
            lastPrice: stock.lastPrice,
            totalCapitalization: stock.totalCapitalization,
            syl: stock.syl,
            totalIncome: finance == null ? null : finance.totalIncome,
            netProfit: finance == null ? null : finance.netProfit
        ],
        rankNo: rank++
    ])
}

log.info("Groovy策略执行完成，结果数量=" + candidates.size())
return candidates.size()
```

---

## 21. 实施步骤建议

### 第一步
引入 Groovy 依赖并完成数据库升级。

### 第二步
重构当前 `StrategyRunService`，抽象执行器接口。

### 第三步
实现 `RuleStrategyExecutor`，保证第一阶段逻辑可继续运行。

### 第四步
实现 `GroovyStrategyExecutor` 与脚本编译缓存。

### 第五步
实现脚本校验接口与页面按钮。

### 第六步
补充运行详情页展示引擎类型、校验状态等信息。

### 第七步
提供 Groovy 示例模板并进行联调测试。

---

## 22. 风险分析与应对

### 22.1 安全风险

风险：脚本能力过强。

应对：

1. `SecureASTCustomizer`
2. 最小 Binding
3. 禁止危险类
4. 超时控制

### 22.2 性能风险

风险：频繁编译脚本。

应对：

1. 按 `scriptHash` 缓存编译结果；
2. 仅在发布版本变化时重新编译。

### 22.3 稳定性风险

风险：脚本质量不稳定。

应对：

1. 发布前校验；
2. 运行日志保留；
3. 异常摘要可视化；
4. RULE/GROOVY 双模式并存。

---

## 23. 结论

第二阶段 Groovy 策略执行器建设，是在第一阶段策略管理闭环基础上的自然升级。通过引入统一执行器接口、Groovy 脚本编译缓存、平台 API、结果输出器和日志输出器，可以将当前“参数驱动筛选”升级为“受控脚本执行平台”。

该设计既能保留第一阶段稳定的规则执行模式，又能为更复杂的选股、因子、信号和回测能力打下基础。

