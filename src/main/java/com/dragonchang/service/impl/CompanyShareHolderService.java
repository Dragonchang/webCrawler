package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.EastMoneyCrawler;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderDetailDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderRecordDTO;
import com.dragonchang.domain.dto.eastmoney.StockHolderRecordListDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.enums.HolderTypeEnum;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.Focus;
import com.dragonchang.domain.po.ShareHolderDetail;
import com.dragonchang.mapper.CompanyShareHolderMapper;
import com.dragonchang.mapper.ShareHolderDetailMapper;
import com.dragonchang.service.ICompanyShareHolderService;
import com.dragonchang.util.DateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
                                shareHolderDetail.setHolderRank(Integer.valueOf(holderDetailDTO.getMc()));
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
                                shareHolderDetail.setHolderRank(Integer.valueOf(holderDetailDTO.getMc()));
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
    public List<CompanyShareHolder> getHolderListByStockId(Integer stockId) {
        return companyShareHolderMapper.selectList(new LambdaQueryWrapper<CompanyShareHolder>()
                .eq(CompanyShareHolder::getCompanyStockId, stockId)
                .orderByDesc(CompanyShareHolder::getReportTime));
    }

    @Override
    public List<ShareHolderDetail> getHolderDetailListByStockId(Integer holderId) {
        return shareHolderDetailMapper.selectList(new LambdaQueryWrapper<ShareHolderDetail>()
                .eq(ShareHolderDetail::getHolderId, holderId));
    }

    @Override
    public List<HolderCompanyListDTO> getHolderListByName(String name) {
        return companyShareHolderMapper.getHolderListByName(name);
    }

    @Override
    public IPage<HolderCompanyListDTO> findPage(HolderDetailRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return companyShareHolderMapper.findPage(page, pageRequest);
    }

    @Override
    public ExcelData exportFlow(HolderDetailRequestDTO request){
        List<HolderCompanyListDTO> list = companyShareHolderMapper.findList(request);
        ExcelData data = new ExcelData();
        String time = DateUtil.localDateTimeFormat(LocalDateTime.now(),DateUtil.DEFAULT_DATE_TIME_FORMAT_SECOND);
        String fileName = "holder" + time;
        data.setSavePath("D:\\");
        data.setFileName(fileName);
        data.setSheetName("holder");
        List<String> titles = new ArrayList();
        titles.add("公司名称");
        titles.add("公司股票代码");
        titles.add("公司最新股价");
        titles.add("公司最新流通市值");
        titles.add("公司最新总市值");
        titles.add("公司最新收益");
        titles.add("股东排名");
        titles.add("持股人/机构名称");
        titles.add("持股数(股)");
        titles.add("占总流通股本持股比例");
        titles.add("增减");
        titles.add("变动比例(%)");
        titles.add("股东类型");
        titles.add("创建时间");
        titles.add("信息发布时间");
        data.setTitles(titles);
        List<List<Object>> rows = new ArrayList();
        for (int i = 0, length = list.size(); i < length; i++) {
            HolderCompanyListDTO hodler = list.get(i);
            List<Object> row = new ArrayList();
            row.add(hodler.getName());
            row.add(hodler.getStockCode());
            row.add(hodler.getLastPrice());
            row.add(hodler.getLastCirculation());
            row.add(hodler.getTotalCapitalization());
            row.add(hodler.getLastIncome());
            row.add(hodler.getHolderRank());
            row.add(hodler.getHolderName());
            row.add(hodler.getHoldCount());
            row.add(hodler.getHoldPercent());
            row.add(hodler.getZj());
            row.add(changePercentToNumber(hodler.getChangePercent()));
            row.add(hodler.getHolderType());
            row.add(hodler.getCreatedTime());
            row.add(hodler.getReportTime());
            rows.add(row);
        }
        data.setRows(rows);
        return data;
    }

    private String changePercentToNumber(String percent) {
        if(StringUtils.isNotBlank(percent)) {
           if(percent.equals("--")) {
               return "0";
           }
            percent = percent.substring(0, percent.length() - 1);
           return percent;
        }
        return null;
    }
}
