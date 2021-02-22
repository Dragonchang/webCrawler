package com.dragonchang.domain.dto.tyc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @program: webcrawler
 * @description:
 * @author: zhangfl
 * @create: 2021-02-20 16:43
 **/
@Data
public class ShareStructureRequestDto {
    private Long companyId;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime startTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime endTime;
}
