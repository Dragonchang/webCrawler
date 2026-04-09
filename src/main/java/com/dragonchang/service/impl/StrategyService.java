package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.StrategyRequestDTO;
import com.dragonchang.domain.dto.StrategySaveRequestDTO;
import com.dragonchang.domain.dto.StrategyValidateRequestDTO;
import com.dragonchang.domain.po.StrategyInfo;
import com.dragonchang.domain.po.StrategyVersion;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.domain.vo.ScriptTemplateVO;
import com.dragonchang.mapper.StrategyInfoMapper;
import com.dragonchang.mapper.StrategyVersionMapper;
import com.dragonchang.service.IStrategyService;
import com.dragonchang.strategy.script.GroovyScriptValidator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class StrategyService implements IStrategyService {

    @Autowired
    private StrategyInfoMapper strategyInfoMapper;

    @Autowired
    private StrategyVersionMapper strategyVersionMapper;

    @Autowired
    private GroovyScriptValidator groovyScriptValidator;

    @Override
    public IPage<StrategyInfo> findPage(StrategyRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return strategyInfoMapper.findPage(page, pageRequest);
    }

    @Override
    public StrategyInfo getById(Long id) {
        return strategyInfoMapper.selectById(id);
    }

    @Override
    public JsonResult saveStrategy(StrategySaveRequestDTO request) {
        if (StringUtils.isBlank(request.getStrategyCode()) || StringUtils.isBlank(request.getStrategyName())) {
            return JsonResult.failure("策略编码和策略名称不能为空");
        }
        StrategyInfo entity;
        LocalDateTime now = LocalDateTime.now();
        if (request.getId() == null) {
            StrategyInfo exists = strategyInfoMapper.selectOne(new LambdaQueryWrapper<StrategyInfo>()
                    .eq(StrategyInfo::getStrategyCode, request.getStrategyCode())
                    .eq(StrategyInfo::getDeleted, 0));
            if (exists != null) {
                return JsonResult.failure("策略编码已存在");
            }
            entity = new StrategyInfo();
            BeanUtils.copyProperties(request, entity);
            entity.setDeleted(0);
            entity.setStatus(0);
            entity.setLatestVersionNo(0);
            entity.setCreatedTime(now);
            entity.setUpdatedTime(now);
            entity.setScriptType(StringUtils.defaultIfBlank(request.getScriptType(), "RULE"));
            entity.setScheduleType(StringUtils.defaultIfBlank(request.getScheduleType(), "MANUAL"));
            strategyInfoMapper.insert(entity);
        } else {
            entity = strategyInfoMapper.selectById(request.getId());
            if (entity == null) {
                return JsonResult.failure("策略不存在");
            }
            StrategyInfo exists = strategyInfoMapper.selectOne(new LambdaQueryWrapper<StrategyInfo>()
                    .eq(StrategyInfo::getStrategyCode, request.getStrategyCode())
                    .ne(StrategyInfo::getId, request.getId())
                    .eq(StrategyInfo::getDeleted, 0));
            if (exists != null) {
                return JsonResult.failure("策略编码已存在");
            }
            String oldScriptType = entity.getScriptType();
            String oldScheduleType = entity.getScheduleType();
            BeanUtils.copyProperties(request, entity);
            entity.setUpdatedTime(now);
            entity.setScriptType(StringUtils.defaultIfBlank(request.getScriptType(), oldScriptType));
            entity.setScheduleType(StringUtils.defaultIfBlank(request.getScheduleType(), oldScheduleType));
            strategyInfoMapper.updateById(entity);
        }
        return JsonResult.success(entity.getId());
    }

    @Override
    public JsonResult publish(Long strategyId) {
        StrategyInfo info = strategyInfoMapper.selectById(strategyId);
        if (info == null || info.getDeleted() != null && info.getDeleted() == 1) {
            return JsonResult.failure("策略不存在");
        }
        if (StringUtils.isBlank(info.getScriptContent())) {
            return JsonResult.failure("策略脚本不能为空");
        }
        Integer nextVersion = info.getLatestVersionNo() == null ? 1 : info.getLatestVersionNo() + 1;
        StrategyVersion version = new StrategyVersion();
        version.setStrategyId(info.getId());
        version.setVersionNo(nextVersion);
        version.setScriptType(info.getScriptType());
        version.setScriptContent(info.getScriptContent());
        version.setParamSchema(info.getParamSchema());
        version.setDefaultParams(info.getDefaultParams());
        version.setUniverseConfig(info.getUniverseConfig());
        version.setScriptHash(DigestUtils.md5DigestAsHex(StringUtils.defaultString(info.getScriptContent()).getBytes()));
        version.setIsPublished(1);
        version.setCreatedTime(LocalDateTime.now());
        strategyVersionMapper.insert(version);

        info.setLatestVersionNo(nextVersion);
        info.setPublishedVersionNo(nextVersion);
        if (info.getStatus() == null || info.getStatus() == 0) {
            info.setStatus(1);
        }
        info.setUpdatedTime(LocalDateTime.now());
        strategyInfoMapper.updateById(info);
        return JsonResult.success(version.getId());
    }

    @Override
    public JsonResult changeStatus(Long strategyId, Integer status) {
        StrategyInfo info = strategyInfoMapper.selectById(strategyId);
        if (info == null) {
            return JsonResult.failure("策略不存在");
        }
        info.setStatus(status);
        info.setUpdatedTime(LocalDateTime.now());
        strategyInfoMapper.updateById(info);
        return JsonResult.success();
    }

    @Override
    public JsonResult validateScript(StrategyValidateRequestDTO request) {
        if (!"GROOVY".equalsIgnoreCase(request.getScriptType())) {
            return JsonResult.success();
        }
        JsonResult result = groovyScriptValidator.validate(request.getScriptContent());
        if (request.getId() != null) {
            StrategyInfo info = strategyInfoMapper.selectById(request.getId());
            if (info != null) {
                info.setValidateStatus(result.isSuccess() ? "SUCCESS" : "FAIL");
                info.setValidateMessage(result.isSuccess() ? "校验成功" : result.getMessage());
                info.setUpdatedTime(LocalDateTime.now());
                strategyInfoMapper.updateById(info);
            }
        }
        return result;
    }

    @Override
    public List<ScriptTemplateVO> getGroovyTemplates() {
        List<ScriptTemplateVO> list = new ArrayList<ScriptTemplateVO>();
        ScriptTemplateVO item = new ScriptTemplateVO();
        item.setName("基础价值筛选模板");
        item.setContent("log.info(\"开始执行 Groovy 策略\")\n\ndef minMarketCap = params.minMarketCap ?: 100\ndef maxPe = params.maxPe ?: 30\ndef minPrice = params.minPrice ?: 5\ndef minIncome = params.minIncome ?: 1000000000\ndef limitCount = params.limit ?: 20\n\ndef candidates = api.stocks()\n    .findAll { it.lastPrice != null && it.lastPrice >= minPrice }\n    .findAll { it.totalCapitalization != null && it.totalCapitalization >= minMarketCap }\n    .findAll { stock ->\n        if (stock.syl == null || stock.syl.toString().trim().isEmpty()) {\n            return false\n        }\n        try {\n            return new BigDecimal(stock.syl.toString()) <= maxPe\n        } catch (Exception ex) {\n            return false\n        }\n    }\n    .findAll { stock ->\n        def finance = api.finance(stock.id)\n        finance != null && finance.totalIncome != null && finance.totalIncome >= minIncome\n    }\n    .sort { a, b -> b.totalCapitalization <=> a.totalCapitalization }\n    .take(limitCount)\n\nint rank = 1\ncandidates.each { stock ->\n    def finance = api.finance(stock.id)\n    result.add([\n        stockId: stock.id,\n        stockCode: stock.stockCode,\n        stockName: stock.name,\n        actionType: \"WATCH\",\n        score: api.score(stock),\n        reason: \"满足Groovy策略中的市值、价格、市盈率和营收条件\",\n        factorDetail: [\n            lastPrice: stock.lastPrice,\n            totalCapitalization: stock.totalCapitalization,\n            syl: stock.syl,\n            totalIncome: finance == null ? null : finance.totalIncome,\n            netProfit: finance == null ? null : finance.netProfit\n        ],\n        rankNo: rank++\n    ])\n}\n\nlog.info(\"Groovy策略执行完成，结果数量=\" + candidates.size())\nreturn candidates.size()\n");
        list.add(item);
        return list;
    }
}

