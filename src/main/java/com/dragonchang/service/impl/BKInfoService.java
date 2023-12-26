package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.eastmoney.BKInfoDTO;
import com.dragonchang.domain.dto.eastmoney.StockInfoDto;
import com.dragonchang.domain.po.BkInfo;
import com.dragonchang.domain.po.BkStock;
import com.dragonchang.domain.po.CompanyPriceRecord;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ConceptInfo;
import com.dragonchang.domain.po.ConceptStock;
import com.dragonchang.mapper.BKInfoMapper;
import com.dragonchang.mapper.BkStockMapper;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.ConceptInfoMapper;
import com.dragonchang.mapper.ConceptStockMapper;
import com.dragonchang.service.IBKInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2023-12-06 15:34
 **/
@Slf4j
@Service
public class BKInfoService implements IBKInfoService {
    @Autowired
    private EastMoneyCrawler eastMoneyCrawler;

    @Autowired
    private BKInfoMapper bkInfoMapper;

    @Autowired
    private BkStockMapper bkStockMapper;

    @Autowired
    private ConceptInfoMapper conceptInfoMapper;

    @Autowired
    private ConceptStockMapper conceptStockMapper;

    @Autowired
    private CompanyStockMapper companyStockMapper;


    @Override
    public void updateBKInfo() {
        List<BKInfoDTO> bkInfoDTOList = eastMoneyCrawler.getBKList();
        if(bkInfoDTOList!= null && !bkInfoDTOList.isEmpty()) {
            for (BKInfoDTO bkInfoDTO: bkInfoDTOList) {
                BkInfo bkInfo = new BkInfo();
                bkInfo.setBkCode(bkInfoDTO.getF12());
                bkInfo.setBkName(bkInfoDTO.getF14());
                bkInfo.setBkLastPrice(new BigDecimal(bkInfoDTO.getF2()));
                bkInfo.setBkTotalCapitalization(new BigDecimal(bkInfoDTO.getF20()));
                bkInfo.setBkLastUpDown(bkInfoDTO.getF4());
                bkInfo.setBkPriceRate(bkInfoDTO.getF3());
                bkInfo.setBkExchange(bkInfoDTO.getF8());
                BkInfo bkInfoSelect = bkInfoMapper.selectOne(new LambdaQueryWrapper<BkInfo>()
                                .eq(BkInfo::getBkCode, bkInfo.getBkCode()));
                if(bkInfoSelect != null) {
                    bkInfoSelect.setBkCode(bkInfo.getBkCode());
                    bkInfoSelect.setBkName(bkInfo.getBkName());
                    bkInfoSelect.setBkLastPrice(bkInfo.getBkLastPrice());
                    bkInfoSelect.setBkTotalCapitalization(bkInfo.getBkTotalCapitalization());
                    bkInfoSelect.setBkLastUpDown(bkInfo.getBkLastUpDown());
                    bkInfoSelect.setBkPriceRate(bkInfo.getBkPriceRate());
                    bkInfoSelect.setBkExchange(bkInfo.getBkExchange());
                    bkInfoMapper.updateById(bkInfoSelect);
                } else {
                    bkInfoMapper.insert(bkInfo);
                }

                List<StockInfoDto> bkstockList = eastMoneyCrawler.getStockListByBkCode(bkInfo.getBkCode());
                if(bkstockList != null && !bkstockList.isEmpty()) {
                    for (StockInfoDto stockInfoDto: bkstockList) {
                        CompanyStock stock = companyStockMapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                                .eq(CompanyStock::getStockCode, stockInfoDto.getF12()));
                        if(stock != null) {
                            BkStock bkStock = null;
                            if(bkInfoSelect != null) {
                                bkStock = bkStockMapper.selectOne(new LambdaQueryWrapper<BkStock>()
                                        .eq(BkStock::getBkId, bkInfoSelect.getId())
                                        .eq(BkStock::getCompanyStockId, stock.getId()));
                            } else {
                                bkStock = bkStockMapper.selectOne(new LambdaQueryWrapper<BkStock>()
                                        .eq(BkStock::getBkId, bkInfo.getId())
                                        .eq(BkStock::getCompanyStockId, stock.getId()));
                            }
                            if(bkStock == null) {
                                bkStock = new BkStock();
                                if(bkInfoSelect != null) {
                                    bkStock.setBkId(bkInfoSelect.getId());
                                    bkStock.setBkName(bkInfoSelect.getBkName());
                                } else {
                                    bkStock.setBkId(bkInfo.getId());
                                    bkStock.setBkName(bkInfo.getBkName());
                                }
                                bkStock.setCompanyStockId(stock.getId());
                                bkStock.setCompanyStockName(stock.getName());
                                bkStockMapper.insert(bkStock);
                            }
                        }
                    }
                }
            }

        }
    }

    @Override
    public void updateConceptInfo() {
        List<BKInfoDTO> bkInfoDTOList = eastMoneyCrawler.getConceptList();
        if(bkInfoDTOList!= null && !bkInfoDTOList.isEmpty()) {
            for (BKInfoDTO bkInfoDTO: bkInfoDTOList) {
                ConceptInfo bkInfo = new ConceptInfo();
                bkInfo.setBkCode(bkInfoDTO.getF12());
                bkInfo.setBkName(bkInfoDTO.getF14());
                bkInfo.setBkLastPrice(new BigDecimal(bkInfoDTO.getF2()));
                bkInfo.setBkTotalCapitalization(new BigDecimal(bkInfoDTO.getF20()));
                bkInfo.setBkLastUpDown(bkInfoDTO.getF4());
                bkInfo.setBkPriceRate(bkInfoDTO.getF3());
                bkInfo.setBkExchange(bkInfoDTO.getF8());
                ConceptInfo bkInfoSelect = conceptInfoMapper.selectOne(new LambdaQueryWrapper<ConceptInfo>()
                        .eq(ConceptInfo::getBkCode, bkInfo.getBkCode()));
                if(bkInfoSelect != null) {
                    bkInfoSelect.setBkCode(bkInfo.getBkCode());
                    bkInfoSelect.setBkName(bkInfo.getBkName());
                    bkInfoSelect.setBkLastPrice(bkInfo.getBkLastPrice());
                    bkInfoSelect.setBkTotalCapitalization(bkInfo.getBkTotalCapitalization());
                    bkInfoSelect.setBkLastUpDown(bkInfo.getBkLastUpDown());
                    bkInfoSelect.setBkPriceRate(bkInfo.getBkPriceRate());
                    bkInfoSelect.setBkExchange(bkInfo.getBkExchange());
                    conceptInfoMapper.updateById(bkInfoSelect);
                } else {
                    conceptInfoMapper.insert(bkInfo);
                }

                List<StockInfoDto> bkstockList = eastMoneyCrawler.getStockListByConceptCode(bkInfo.getBkCode());
                if(bkstockList != null && !bkstockList.isEmpty()) {
                    for (StockInfoDto stockInfoDto: bkstockList) {
                        CompanyStock stock = companyStockMapper.selectOne(new LambdaQueryWrapper<CompanyStock>()
                                .eq(CompanyStock::getStockCode, stockInfoDto.getF12()));
                        if(stock != null) {
                            ConceptStock bkStock = null;
                            if(bkInfoSelect != null) {
                                bkStock = conceptStockMapper.selectOne(new LambdaQueryWrapper<ConceptStock>()
                                        .eq(ConceptStock::getBkId, bkInfoSelect.getId())
                                        .eq(ConceptStock::getCompanyStockId, stock.getId()));
                            } else {
                                bkStock = conceptStockMapper.selectOne(new LambdaQueryWrapper<ConceptStock>()
                                        .eq(ConceptStock::getBkId, bkInfo.getId())
                                        .eq(ConceptStock::getCompanyStockId, stock.getId()));
                            }
                            if(bkStock == null) {
                                bkStock = new ConceptStock();
                                if(bkInfoSelect != null) {
                                    bkStock.setBkId(bkInfoSelect.getId());
                                    bkStock.setBkName(bkInfoSelect.getBkName());
                                } else {
                                    bkStock.setBkId(bkInfo.getId());
                                    bkStock.setBkName(bkInfo.getBkName());
                                }
                                bkStock.setCompanyStockId(stock.getId());
                                bkStock.setCompanyStockName(stock.getName());
                                conceptStockMapper.insert(bkStock);
                            }
                        }
                    }
                }
            }
        }
    }
}
