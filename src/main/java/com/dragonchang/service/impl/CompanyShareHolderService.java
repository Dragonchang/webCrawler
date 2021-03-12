package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderDetailDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderRecordDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderRecordListDTO;
import com.dragonchang.domain.enums.HolderTypeEnum;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.ShareHolderDetail;
import com.dragonchang.mapper.CompanyShareHolderMapper;
import com.dragonchang.mapper.ShareHolderDetailMapper;
import com.dragonchang.service.ICompanyShareHolderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-03-10 10:13
 **/
@Slf4j
@Service
public class CompanyShareHolderService implements ICompanyShareHolderService {

    @Autowired
    private EastMoneyCrawler eastMoneyCrawler;

    @Autowired
    private CompanyShareHolderMapper companyShareHolderMapper;

    @Autowired
    private ShareHolderDetailMapper shareHolderDetailMapper;

    @Override
    public void syncStockHolderByCode(CompanyStock companyStock) {
        if (companyStock == null) {
            log.error("公司信息为空");
            return;
        }
        StockHolderRecordListDTO stockHodlerInfoByCode = eastMoneyCrawler.getStockHodlerInfoByCode(companyStock.getStockCode());
        if (stockHodlerInfoByCode != null) {
            List<StockHolderRecordDTO> sdltgd = stockHodlerInfoByCode.getSdltgd();
            if (CollectionUtils.isNotEmpty(sdltgd)) {
                for (StockHolderRecordDTO stockHolderRecordDTO : sdltgd) {
                    CompanyShareHolder shareHolder = companyShareHolderMapper.selectOne(new LambdaQueryWrapper<CompanyShareHolder>()
                            .eq(CompanyShareHolder::getCompanyStockId, companyStock.getId())
                            .eq(CompanyShareHolder::getReportTime, stockHolderRecordDTO.getRq())
                            .eq(CompanyShareHolder::getHolderType,HolderTypeEnum.LT.getCode()));
                    if (shareHolder == null) {
                        shareHolder = new CompanyShareHolder();
                        shareHolder.setCompanyStockId(companyStock.getId());
                        shareHolder.setHolderType(HolderTypeEnum.LT.getCode());
                        shareHolder.setReportTime(stockHolderRecordDTO.getRq());
                        companyShareHolderMapper.insert(shareHolder);
                        List<StockHolderDetailDTO> sdltgdDetail = stockHolderRecordDTO.getSdltgd();
                        if (CollectionUtils.isNotEmpty(sdltgd)) {
                            for(StockHolderDetailDTO holderDetailDTO : sdltgdDetail) {
                                ShareHolderDetail shareHolderDetail = new ShareHolderDetail();
                                shareHolderDetail.setHolderId(shareHolder.getId());
                                shareHolderDetail.setHolderRank(Long.valueOf(holderDetailDTO.getMc()));
                                shareHolderDetail.setHolderName(holderDetailDTO.getGdmc());
                                shareHolderDetail.setHolderType(holderDetailDTO.getGdxz());
                                shareHolderDetail.setStockType(holderDetailDTO.getGflx());
                                shareHolderDetail.setHoldCount(Long.valueOf(holderDetailDTO.getCgs().replace(",","")));
                                shareHolderDetail.setHoldPercent(holderDetailDTO.getZltgbcgbl());
                                shareHolderDetail.setZj(holderDetailDTO.getZj());
                                shareHolderDetail.setChangePercent(holderDetailDTO.getBdbl());
                                shareHolderDetailMapper.insert(shareHolderDetail);
                            }
                        } else {
                            log.warn("流通股东信息列表为空" + companyStock.getStockCode() + stockHolderRecordDTO.getRq());
                        }

                    } else {
                        log.warn("流通股东信息已经更新" + companyStock.getStockCode() + stockHolderRecordDTO.getRq());
                    }
                }
            } else {
                log.warn("流通股东为空！" + companyStock.getStockCode());
            }

            List<StockHolderRecordDTO> sdgd = stockHodlerInfoByCode.getSdgd();
            if (CollectionUtils.isNotEmpty(sdgd)) {
                for (StockHolderRecordDTO stockHolderRecordDTO : sdgd) {
                    CompanyShareHolder shareHolder = companyShareHolderMapper.selectOne(new LambdaQueryWrapper<CompanyShareHolder>()
                            .eq(CompanyShareHolder::getCompanyStockId, companyStock.getId())
                            .eq(CompanyShareHolder::getReportTime, stockHolderRecordDTO.getRq())
                            .eq(CompanyShareHolder::getHolderType,HolderTypeEnum.GD.getCode()));
                    if (shareHolder == null) {
                        shareHolder = new CompanyShareHolder();
                        shareHolder.setCompanyStockId(companyStock.getId());
                        shareHolder.setHolderType(HolderTypeEnum.GD.getCode());
                        shareHolder.setReportTime(stockHolderRecordDTO.getRq());
                        companyShareHolderMapper.insert(shareHolder);
                        List<StockHolderDetailDTO> sdltgdDetail = stockHolderRecordDTO.getSdgd();
                        if (CollectionUtils.isNotEmpty(sdltgd)) {
                            for(StockHolderDetailDTO holderDetailDTO : sdltgdDetail) {
                                ShareHolderDetail shareHolderDetail = new ShareHolderDetail();
                                shareHolderDetail.setHolderId(shareHolder.getId());
                                shareHolderDetail.setHolderRank(Long.valueOf(holderDetailDTO.getMc()));
                                shareHolderDetail.setHolderName(holderDetailDTO.getGdmc());
                                shareHolderDetail.setHolderType(holderDetailDTO.getGdxz());
                                shareHolderDetail.setStockType(holderDetailDTO.getGflx());
                                shareHolderDetail.setHoldCount(Long.valueOf(holderDetailDTO.getCgs().replace(",","")));
                                shareHolderDetail.setHoldPercent(holderDetailDTO.getZltgbcgbl());
                                shareHolderDetail.setZj(holderDetailDTO.getZj());
                                shareHolderDetail.setChangePercent(holderDetailDTO.getBdbl());
                                shareHolderDetailMapper.insert(shareHolderDetail);
                            }
                        } else {
                            log.warn("股东信息列表为空" + companyStock.getStockCode() + stockHolderRecordDTO.getRq());
                        }

                    } else {
                        log.warn("股东信息已经更新" + companyStock.getStockCode() + stockHolderRecordDTO.getRq());
                    }
                }
            } else {
                log.warn("股东为空！" + companyStock.getStockCode());
            }
        } else {
            log.error("sync Stock Holder failed: " + companyStock.getStockCode());
        }
    }

    @Override
    public List<CompanyShareHolder> getHodlderListByStockId(Long stockId) {
        return companyShareHolderMapper.selectList(new LambdaQueryWrapper<CompanyShareHolder>()
                .eq(CompanyShareHolder::getCompanyStockId, stockId)
                .orderByDesc(CompanyShareHolder::getReportTime));
    }

    @Override
    public List<ShareHolderDetail> getHodlderDetailListByStockId(Long holderId) {
        return shareHolderDetailMapper.selectList(new LambdaQueryWrapper<ShareHolderDetail>()
                .eq(ShareHolderDetail::getHolderId, holderId));
    }

    @Override
    public List<HolderCompanyListDTO> getHolderListByName(String name) {
        return companyShareHolderMapper.getHolderListByName(name);
    }
}
