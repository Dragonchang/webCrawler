package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Focus;

import java.util.List;

/**
 * @author 63474
 */
public interface IFocusService {

    /**
     * 分页查询公司信息
     * @param pageRequest
     * @return
     */
    IPage<Focus> findPage(CompanyRequestDTO pageRequest);
}
