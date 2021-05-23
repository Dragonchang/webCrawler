package com.dragonchang.service.impl;

import com.dragonchang.domain.dto.FinanceAnalysisResponseDTO;
import com.dragonchang.domain.dto.RecommendAnalysisDTO;
import com.dragonchang.mapper.FinanceAnalysisMapper;
import com.dragonchang.service.IRecommendService;
import com.dragonchang.util.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: webcrawler
 * @description: 推荐算法服务
 * @author: zhangfl
 * @create: 2021-05-21 21:31
 **/
public class RecommendService implements IRecommendService {

    @Autowired
    private FinanceAnalysisMapper financeAnalysisMapper;

    private static Boolean filterWithoutPioneer = true;
    /**
     * 1.通过限定条件查出符合财务状况的公司
     *  营业额要分层
     *  100亿以上
     *  50~100亿
     *  10~50亿
     * 2.循环遍历1步骤过滤出来的公司
     *   检查在当前时间的季度是否暴雷
     *   营业额和扣非同比低于1%
     *
     */
    @Override
    public Map<String, Object> recommend() {
        Map<String, Object> maps = new HashMap<String, Object>();
        RecommendAnalysisDTO recommendAnalysisDTO = new RecommendAnalysisDTO();

        List<FinanceAnalysisResponseDTO> totalIncome100 = financeAnalysisMapper.findRecommendList(recommendAnalysisDTO);
        filterLandmine(totalIncome100);
        List<FinanceAnalysisResponseDTO> totalIncome50And100 = financeAnalysisMapper.findRecommendList(recommendAnalysisDTO);
        filterLandmine(totalIncome50And100);
        List<FinanceAnalysisResponseDTO> totalIncome10And50 = financeAnalysisMapper.findRecommendList(recommendAnalysisDTO);
        filterLandmine(totalIncome10And50);
        return maps;
    }


    /**
     * 过滤暴雷公司
     * @param list
     */
    private List<FinanceAnalysisResponseDTO> filterLandmine(List<FinanceAnalysisResponseDTO> list) {
        String now = DateUtil.formatDate(new Date());
        int quarter = DateUtil.getCurrentQuarter();

        List<FinanceAnalysisResponseDTO> ret = new ArrayList<>();
        for (FinanceAnalysisResponseDTO financeAnalysisResponseDTO : list) {
            if(filterWithoutPioneer && checkStockCodeIsPioneer(financeAnalysisResponseDTO.getStockCode())) {
                continue;
            }
            //如果为第一季度直接将记录加入到推荐列表中
            if(quarter == 1) {
                ret.add(financeAnalysisResponseDTO);
            } else if(quarter == 2) {

            }else if(quarter == 3){

            }else if(quarter == 4) {
                //

            }
        }

        return ret;
    }

    /**
     * 检查是否创业板公司
     * @param stockCode
     * @return
     */
    private boolean checkStockCodeIsPioneer(String stockCode) {
        if(StringUtils.isNotBlank(stockCode)) {
            if(stockCode.length() == 6) {
                if(stockCode.startsWith("300") || stockCode.startsWith("688")) {
                    return true;
                }
            }
        }
        return false;
    }
}
