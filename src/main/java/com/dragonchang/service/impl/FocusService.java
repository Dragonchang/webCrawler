package com.dragonchang.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dragonchang.domain.dto.FocusAddRequestDTO;
import com.dragonchang.domain.dto.FocusDTO;
import com.dragonchang.domain.dto.tyc.CompanyRequestDTO;
import com.dragonchang.domain.po.CompanyStock;
import com.dragonchang.domain.po.Focus;
import com.dragonchang.domain.vo.JsonResult;
import com.dragonchang.mapper.CompanyStockMapper;
import com.dragonchang.mapper.FocusMapper;
import com.dragonchang.service.IFocusService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;


/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-18 11:38
 **/
@Slf4j
@Service
public class FocusService implements IFocusService {

    @Autowired
    private FocusMapper mapper;

    @Autowired
    private CompanyStockMapper companyStockMapper;

    @Override
    public IPage<FocusDTO> findPage(CompanyRequestDTO pageRequest) {
        Page page = new Page();
        page.setCurrent(pageRequest.getPage());
        page.setSize(pageRequest.getSize());
        return mapper.findPage(page, pageRequest);
    }

    @Override
    public JsonResult delete(int id) {
        mapper.deleteById(id);
        return JsonResult.success();
    }

    @Override
    public JsonResult add(FocusAddRequestDTO addRequestDTO) {
        LambdaQueryWrapper<Focus> wrapper= new LambdaQueryWrapper<Focus>();
        if(addRequestDTO.getStockCompanyId() != null) {
            wrapper = wrapper.eq(Focus::getStockCompanyId, addRequestDTO.getStockCompanyId());
        }
        if(!StringUtils.isEmpty(addRequestDTO.getCompanyName())) {
            wrapper = wrapper.eq(Focus::getCompanyName, addRequestDTO.getCompanyName());
        }
        List<Focus> focusList = mapper.selectList(wrapper);
        if(focusList != null && focusList.size() >0) {
            return JsonResult.failure("已经被关注");
        }


        Focus focus = new Focus();
        BeanUtils.copyProperties(addRequestDTO, focus);
        if(addRequestDTO.getStockCompanyId() != null) {
            CompanyStock stock = companyStockMapper.selectById(addRequestDTO.getStockCompanyId());
            focus.setFocusPrice(stock.getLastPrice());
        } else {
            focus.setFocusPrice(new BigDecimal(0));
        }
        mapper.insert(focus);
        return JsonResult.success();
    }
}
