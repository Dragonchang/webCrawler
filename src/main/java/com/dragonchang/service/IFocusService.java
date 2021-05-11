package com.dragonchang.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.dragonchang.domain.dto.FocusAddRequestDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.Focus;
import com.dragonchang.domain.vo.JsonResult;

import java.util.List;

/**
 * @author 63474
 */
public interface IFocusService {

    /**
     * 分页查询关注公司信息
     * @param pageRequest
     * @return
     */
    IPage<Focus> findPage(CompanyRequestDTO pageRequest);

    /**
     * 删除关注
     * @param id
     */
    JsonResult delete(int id);


    /**
     * 添加关注
     * @param addRequestDTO
     * @return
     */
    JsonResult add(FocusAddRequestDTO addRequestDTO);

}
