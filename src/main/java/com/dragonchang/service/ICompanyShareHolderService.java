package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.ExcelData;
import com.dragonchang.domain.dto.HolderCompanyListDTO;
import com.dragonchang.domain.dto.HolderDetailRequestDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.CompanyShareHolder;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.Focus;
import com.dragonchang.domain.po.ShareHolderDetail;

import java.util.List;

/**
 * @author 63474
 */
public interface ICompanyShareHolderService {

    /**
     * 同步公司股票列表信息
     */
    void syncStockHolderByCode(CompanyStock companyStock);

    /**
     * 通过stockid获取股东发布列表
     * @param stockId
     * @return
     */
    List<CompanyShareHolder> getHolderListByStockId(Integer stockId);

    /**
     * 获取持股股东详情
     * @param holderId
     * @return
     */
    List<ShareHolderDetail> getHolderDetailListByStockId(Integer holderId);

    /**
     * 根据股东名称获取所有持股信息
     * @param name
     * @return
     */
    List<HolderCompanyListDTO> getHolderListByName(String name);

    /**
     * 分页查询关注公司信息
     * @param pageRequest
     * @return
     */
    IPage<HolderCompanyListDTO> findPage(HolderDetailRequestDTO pageRequest);

    /**
     * 导出持有穿透
     * @param request
     * @return
     */
    ExcelData exportFlow(HolderDetailRequestDTO request);
}
