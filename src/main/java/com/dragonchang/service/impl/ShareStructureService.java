package com.dragonchang.service.impl;

import com.dragonchang.domain.dto.ShareStructureDTO;
import com.dragonchang.domain.dto.tyc.ShareStructureRequestDto;
import com.dragonchang.domain.po.ShareStructure;
import com.dragonchang.mapper.ShareStructureMapper;
import com.dragonchang.service.IShareStructureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-20 17:29
 **/
@Slf4j
@Service
public class ShareStructureService implements IShareStructureService {

    @Autowired
    private ShareStructureMapper shareStructureMapper;

    @Override
    public List<ShareStructureDTO> getShareStructure(ShareStructureRequestDto requestDto) {
        List<ShareStructureDTO> shareStructures = shareStructureMapper.findList(requestDto);
        return shareStructures;
    }
}
