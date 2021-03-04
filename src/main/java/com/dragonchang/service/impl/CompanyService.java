package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.crawler.TycCrawler;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.dto.tyc.ShareCompanyDto;
import com.dragonchang.domain.dto.tyc.ShareCompanyListDto;
import com.dragonchang.domain.po.Company;
import com.dragonchang.domain.po.ShareStructure;
import com.dragonchang.mapper.CompanyMapper;
import com.dragonchang.mapper.ShareStructureMapper;
import com.dragonchang.service.ICompanyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-18 11:38
 **/
@Slf4j
@Service
public class CompanyService implements ICompanyService {

    @Autowired
    private CompanyMapper mapper;

    @Autowired
    private ShareStructureMapper shareStructureMapper;

    @Autowired
    private TycCrawler tycCrawler;

    @Override
    public IPage<Company> findPage(CompanyRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return mapper.findPage(page, pageRequest);
    }

    @Override
    public void syncShareInfoWithCompanyId(Long companyId) {
        Company company = mapper.selectById(companyId);
        if (company == null) {
            log.error("db with empty info for " + companyId);
            return;
        }

        List<ShareCompanyDto> investorList = new ArrayList<>();
        Integer retryCount = 0;
        while (getShareList(company, investorList) == false) {
            retryCount = retryCount + 1;
            log.warn("get share failed retry !!" + retryCount);
        }
        log.info("get share success!!");
        insertShareList(company, investorList);
    }

    @Override
    public Company getCompanyById(Long companyId) {
        return mapper.selectById(companyId);
    }

    @Override
    public List<Company> getAllFocusCompanyList() {
        return mapper.selectList(null);
    }

    private boolean getShareList(Company company, List<ShareCompanyDto> investorList) {
        ShareCompanyListDto shareCompanyInfo = tycCrawler.getShareCompanyInfo(company.getTycId());
        if (shareCompanyInfo != null) {
            List<ShareCompanyDto> tempInvestorList = shareCompanyInfo.getInvestorList();
            if (CollectionUtils.isNotEmpty(tempInvestorList)) {
                investorList.addAll(tempInvestorList);
                return true;
            } else {
                log.warn("return empty info");
            }
        } else {
            log.error("companyId :" + company.getId() + " return empty info");
        }
        return false;
    }

    private void insertShareList(Company company, List<ShareCompanyDto> investorList) {
        if (CollectionUtils.isNotEmpty(investorList)) {
            for (ShareCompanyDto shareCompanyDto : investorList) {
                ShareStructure shareStructure = new ShareStructure();
                shareStructure.setCompanyId(company.getId());
                shareStructure.setShareCompanyName(shareCompanyDto.getName());
                shareStructure.setShareCompanyAmount(shareCompanyDto.getAmount());
                shareStructure.setShareCompanyType(shareCompanyDto.getCompanyType());
                shareStructure.setShareCompanyBondType(shareCompanyDto.getBondType());
                shareStructure.setShareCompanyFinanceLabel(shareCompanyDto.getFinanceLabel());
                shareStructure.setShareCompanyPercent(shareCompanyDto.getPercent());
                shareStructure.setShareCompanyBrand(shareCompanyDto.getBrand());
                shareStructureMapper.insert(shareStructure);
            }
        }
    }
}
