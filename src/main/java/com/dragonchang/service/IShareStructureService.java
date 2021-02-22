package com.dragonchang.service;

import com.dragonchang.domain.dto.ShareStructureDTO;
import com.dragonchang.domain.dto.tyc.ShareStructureRequestDto;
import com.dragonchang.domain.po.ShareStructure;

import java.util.List;

public interface IShareStructureService {

    /**
     * 获取公司对应的股权穿透信息
     * @param requestDto
     * @return
     */
    List<ShareStructureDTO> getShareStructure(ShareStructureRequestDto requestDto);
}
